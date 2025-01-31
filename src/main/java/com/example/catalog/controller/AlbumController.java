package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final DataSourceService dataSourceService;

    @Autowired
    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() throws IOException {
        List<Album> albums = dataSourceService.getAllAlbums();
        if (albums.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();  // 400 Bad Request if ID is invalid
        }
        Album album = dataSourceService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if album does not exist
        }
        return ResponseEntity.ok(album);  // 200 OK with the album data
    }

    @PostMapping
    public ResponseEntity<?> createAlbum(@RequestBody Album album) {
        try {
            Album savedAlbum = dataSourceService.saveAlbum(album);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Error creating album: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbumById(@PathVariable String id, @RequestBody Album updatedAlbum) {
        try {
            boolean updated = dataSourceService.updateAlbumById(id, updatedAlbum);
            if (updated) {
                return ResponseEntity.ok("Album updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error updating album: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlbumById(@PathVariable String id) {
        try {
            boolean deleted = dataSourceService.deleteAlbumById(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting album: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<?> getTracksByAlbumId(@PathVariable String id) {
        try {
            List<Track> tracks = dataSourceService.getTracksByAlbumId(id);
            if (tracks == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
            }
            return ResponseEntity.ok(tracks);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error retrieving tracks: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/tracks")
    public ResponseEntity<?> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) {
        try {
            Album updatedAlbum = dataSourceService.addTrack(id, track);
            if (updatedAlbum == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Album not found"));
            }

            return ResponseEntity.ok(updatedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Error adding track: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<?> updateTrackInAlbum(
            @PathVariable String id,
            @PathVariable String track_id,
            @RequestBody Track updatedTrack) {
        try {
            Album updatedAlbum = dataSourceService.updateTrack(id, track_id, updatedTrack);
            if (updatedAlbum == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Album or Track not found"));
            }

            return ResponseEntity.ok(updatedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Error updating track: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<?> deleteTrackFromAlbum(@PathVariable String id, @PathVariable String track_id) {
        try {
            boolean deleted = dataSourceService.deleteTrack(id, track_id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Album or Track not found"));
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Error deleting track: " + e.getMessage()));
        }
    }











}
