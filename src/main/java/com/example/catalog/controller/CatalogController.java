package com.example.catalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/popularSongs")
    public JsonNode getPopularSongs() throws IOException {
        ClassPathResource resource = new ClassPathResource("src/main/data/popular_songs.json");
        return objectMapper.readTree(resource.getFile());
    }

    @GetMapping("/popularArtists")
    public JsonNode getPopularArtists() throws IOException {
        ClassPathResource resource = new ClassPathResource("src/main/data/popular_artists.json");
        return objectMapper.readTree(resource.getFile());
    }

}