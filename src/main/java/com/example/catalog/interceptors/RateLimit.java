package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimit implements HandlerInterceptor {

    @Value("${rate-limit.algo}")
    public String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    public int rateLimitRPM;

    private final Map<String, FixedWindow> fixedWindowClients = new ConcurrentHashMap<>();
    private final Map<String, SlidingWindow> slidingWindowClients = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        // Exclude /internal endpoint from rate limiting
        if (request.getRequestURI().equals("/internal")) {
            return true;
        }

        boolean allowed;
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            allowed = isAllowedFixedWindow(clientIp, System.currentTimeMillis());
        } else if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            allowed = isAllowedSlidingWindow(clientIp, System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("Unsupported rate limit algorithm: " + rateLimitAlgo);
        }

        if (!allowed) {
            response.setStatus(429);
            response.setHeader("X-Rate-Limit-Remaining", "0");
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(getRetryAfterSeconds(clientIp)));
            return false;
        }

        int remaining = getRemainingRequests(clientIp);
        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remaining));
        return true;
    }

    private boolean isAllowedFixedWindow(String clientIp, long currentTime) {
        FixedWindow window = fixedWindowClients.computeIfAbsent(clientIp, k -> new FixedWindow(rateLimitRPM));
        synchronized (window) {
            return window.isAllowed(currentTime);
        }
    }

    private boolean isAllowedSlidingWindow(String clientIp, long currentTime) {
        SlidingWindow window = slidingWindowClients.computeIfAbsent(clientIp, k -> new SlidingWindow(rateLimitRPM));
        synchronized (window) {
            return window.isAllowed(currentTime);
        }
    }

    private int getRetryAfterSeconds(String clientIp) {
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            FixedWindow window = fixedWindowClients.get(clientIp);
            if (window != null) {
                return window.getRetryAfterSeconds();
            }
        } else if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            SlidingWindow window = slidingWindowClients.get(clientIp);
            if (window != null) {
                return window.getRetryAfterSeconds();
            }
        }
        return 0;
    }

    private int getRemainingRequests(String clientIp) {
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            FixedWindow window = fixedWindowClients.get(clientIp);
            if (window != null) {
                return window.getRemainingRequests();
            }
        } else if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            SlidingWindow window = slidingWindowClients.get(clientIp);
            if (window != null) {
                return window.getRemainingRequests();
            }
        }
        return 0;
    }

    private static class FixedWindow {
        private final int maxRequests;
        private int count;
        private long windowStart;

        public FixedWindow(int maxRequests) {
            this.maxRequests = maxRequests;
            this.count = 0;
            this.windowStart = System.currentTimeMillis();
        }

        public boolean isAllowed(long currentTime) {
            long windowDuration = 60000; // 1 minute
            if (currentTime - windowStart >= windowDuration) {
                windowStart = currentTime;
                count = 1;
                return true;
            } else if (count < maxRequests) {
                count++;
                return true;
            }
            return false;
        }

        public int getRetryAfterSeconds() {
            return (int) ((windowStart + 60000 - System.currentTimeMillis()) / 1000);
        }

        public int getRemainingRequests() {
            return maxRequests - count;
        }
    }

    private static class SlidingWindow {
        private final int maxRequests;
        private final long[] timestamps;
        private int head;
        private int count;

        public SlidingWindow(int maxRequests) {
            this.maxRequests = maxRequests;
            this.timestamps = new long[maxRequests];
            this.head = 0;
            this.count = 0;
        }

        public boolean isAllowed(long currentTime) {
            long windowDuration = 60000; // 1 minute
            synchronized (this) {
                if (count < maxRequests) {
                    timestamps[head] = currentTime;
                    head = (head + 1) % maxRequests;
                    count++;
                    return true;
                } else if (currentTime - timestamps[head] >= windowDuration) {
                    timestamps[head] = currentTime;
                    head = (head + 1) % maxRequests;
                    return true;
                }
                return false;
            }
        }

        public int getRetryAfterSeconds() {
            return (int) ((timestamps[head] + 60000 - System.currentTimeMillis()) / 1000);
        }

        public int getRemainingRequests() {
            return Math.max(0, maxRequests - count);
        }
    }
}
