package com.example.catalog.model;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Song {
    private String id;
    private String name;
    private boolean explicit;
    private String uri;
    private int popularity;
    private Album album;
    private List<Artist> artists;

    @JsonProperty("duration_ms")
    private int durationMs;

    public Song() {}

    public Song(String id, String name, boolean explicit, String uri, int durationMs, int popularity, Album album, List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.explicit = explicit;
        this.uri = uri;
        this.durationMs = durationMs;
        this.popularity = popularity;
        this.album = album;
        this.artists = artists;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public List<Artist> getArtists() { return artists; }
    public void setArtists(List<Artist> artists) { this.artists = artists; }
}
