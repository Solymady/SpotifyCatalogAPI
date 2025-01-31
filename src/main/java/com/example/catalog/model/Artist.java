package com.example.catalog.model;
import java.util.List;

public class Artist {
    private String id;
    private String name;
    private int followers;
    private List<String> genres;
    private List<Image> images;
    private int popularity;
    private String uri;

    public Artist() {}

    public Artist(String id, String name, int followers, List<String> genres, List<Image> images, int popularity, String uri) {
        this.id = id;
        this.name = name;
        this.followers = followers;
        this.genres = genres;
        this.images = images;
        this.popularity = popularity;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
