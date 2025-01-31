package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() throws IOException {
        List<Artist> artists = dataSourceService.getAllArtists();

        if (artists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<?> createArtist(@RequestBody Artist artist) {
        try {
            Artist savedArtist = dataSourceService.saveArtist(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error saving artist: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable String id, @RequestBody Artist updatedArtist) {
        try {
            Artist artist = dataSourceService.updateArtist(id, updatedArtist);
            if (artist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Artist not found"));
            }
            return ResponseEntity.ok(artist);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error updating artist: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable String id) {
        try {
            boolean deleted = dataSourceService.deleteArtist(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Artist not found"));
            }
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error deleting artist: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<?> getAlbumsByArtistId(@PathVariable String id) {
        try {
            List<Album> albums = dataSourceService.getAlbumsByArtistId(id);
            if (albums.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "No albums found for the artist"));
            }
            return ResponseEntity.ok(albums);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error retrieving albums: " + e.getMessage()));
        }
    }


}