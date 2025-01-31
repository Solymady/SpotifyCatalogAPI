package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Track {
    private String id;
    private String name;
    private boolean explicit;

    @JsonProperty("duration_ms")  // Maps "duration_ms" in JSON to "durationMs" in Java
    private int durationMs;

    private String uri;

    public Track() {}

    public Track(String id, String name, boolean explicit, int durationMs, String uri) {
        this.id = id;
        this.name = name;
        this.explicit = explicit;
        this.durationMs = durationMs;
        this.uri = uri;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }

    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
}
