package com.example.catalog;

import com.example.catalog.interceptors.RateLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimitTest {

    private RateLimit rateLimit;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        rateLimit = new RateLimit();
        // Set rate limiting algorithm and RPM
        rateLimit.rateLimitAlgo = "fixed"; // Change to "moving" to test the sliding window
        rateLimit.rateLimitRPM = 5; // Allow 5 requests per minute

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testFixedWindowRateLimiting() throws Exception {
        String clientIp = "192.168.1.1";
        request.setRemoteAddr(clientIp);
        request.setRequestURI("/api");

        // Simulate 5 allowed requests
        for (int i = 0; i < 5; i++) {
            boolean allowed = rateLimit.preHandle(request, response, null);
            assertTrue(allowed);
            assertEquals(String.valueOf(5 - (i + 1)), response.getHeader("X-Rate-Limit-Remaining"));
        }

        // Simulate 6th request (should be blocked)
        boolean allowed = rateLimit.preHandle(request, response, null);
        assertEquals(false, allowed);
        assertEquals("0", response.getHeader("X-Rate-Limit-Remaining"));
        assertEquals("429", String.valueOf(response.getStatus()));
        assertTrue(response.getHeader("X-Rate-Limit-Retry-After-Seconds") != null);
    }


    @Test
    public void testNoRateLimitingForInternalEndpoint() throws Exception {
        String clientIp = "192.168.1.3";
        request.setRemoteAddr(clientIp);
        request.setRequestURI("/internal"); // Internal endpoint should bypass rate limiting

        // Simulate 10 requests
        for (int i = 0; i < 10; i++) {
            boolean allowed = rateLimit.preHandle(request, response, null);
            assertTrue(allowed);
            // No headers should be set for internal endpoint
            assertEquals(null, response.getHeader("X-Rate-Limit-Remaining"));
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    public void testSlidingWindowRateLimiting() throws Exception {
        rateLimit.rateLimitAlgo = "moving"; // Use sliding window
        request.setRemoteAddr("192.168.1.4");
        request.setRequestURI("/api");

        // Simulate 5 allowed requests
        for (int i = 0; i < 5; i++) {
            response = new MockHttpServletResponse(); // Reset response
            assertTrue(rateLimit.preHandle(request, response, null), "Request " + (i + 1) + " should be allowed");
        }

        // Simulate 6th request (should be blocked)
        response = new MockHttpServletResponse(); // Reset response
        boolean allowed = rateLimit.preHandle(request, response, null);
        assertEquals(false, allowed, "6th request should be blocked");

        // Wait for sliding window expiration
        TimeUnit.SECONDS.sleep(4); // Ensure window expires
        response = new MockHttpServletResponse(); // Reset response
        assertFalse(rateLimit.preHandle(request, response, null), "Request after window expiration should be allowed");
    }

}
