package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class DatabaseDataSourceService implements DataSourceService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Artist getArtistById(String id) throws IOException {
        return null;
    }

    @Override
    public Artist saveArtist(Artist artist) throws IOException {
        return null;
    }

    @Override
    public Artist updateArtist(String id, Artist updatedArtist) throws IOException {
        return null;
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {
        return false;
    }

    @Override
    public List<Album> getAlbumsByArtistId(String id) throws IOException {
        return List.of();
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        return List.of();
    }

    @Override
    public Song getSongById(String id) throws IOException {
        return null;
    }

    @Override
    public Song saveSong(Song song) throws IOException {
        return null;
    }

    @Override
    public Song updateSong(String id, Song updatedSong) throws IOException {
        return null;
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        return false;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        JsonNode albumsNode = loadJsonData("src/main/data/albums.json");

        return objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        return null;
    }

    @Override
    public Album saveAlbum(Album album) throws IOException {
        return null;
    }

    @Override
    public boolean updateAlbumById(String id, Album updatedAlbum) throws IOException {
        return false;
    }

    @Override
    public boolean deleteAlbumById(String id) throws IOException {
        return false;
    }

    @Override
    public List<Track> getTracksByAlbumId(String id) throws IOException {
        return List.of();
    }

    @Override
    public Album addTrack(String id, Track track) throws IOException {
        return null;
    }

    @Override
    public Album updateTrack(String id, String trackId, Track updatedTrack) throws IOException {
        return null;
    }

    @Override
    public boolean deleteTrack(String id, String trackId) throws IOException {
        return false;
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        return List.of();
    }

    private JsonNode loadJsonData(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        if (!resource.exists()) {
            throw new IOException("File not found: " + path);
        }

        return objectMapper.readTree(resource.getFile());
    }
}
