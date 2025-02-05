package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Album {
    private String id;
    private String name;
    private String uri;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("total_tracks")
    private int totalTracks;

    private List<Image> images;

    @JsonProperty("tracks")
    private List<Track> tracks;

    public Album() {}

    public Album(String id, String name, String uri, String releaseDate, int totalTracks, List<Image> images, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
        this.images = images;
        this.tracks = tracks;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }

    public List<Image> getImages() { return images; }
    public void setImages(List<Image> images) { this.images = images; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }
}
