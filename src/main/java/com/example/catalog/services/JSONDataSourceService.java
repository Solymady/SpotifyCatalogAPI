package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;


@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //albums
    @Override
    public List<Album> getAllAlbums() throws IOException {
        JsonNode rootNode = loadJsonData("src/data/albums.json");

        // Convert object to array
        List<Album> albums = new ArrayList<>();
        if (rootNode.isObject()) {
            for (JsonNode albumNode : rootNode) {
                Album album = objectMapper.treeToValue(albumNode, Album.class);
                albums.add(album);
            }
        } else {
            throw new IOException("Invalid JSON format: Expected an object with album IDs as keys");
        }

        return albums;
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        List<Album> albums = getAllAlbums();  // Ensure it fetches all albums
        return albums.stream()
                .filter(album -> album.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Album saveAlbum(Album album) throws IOException {
        File file = new File("src/data/albums.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Create directories if needed
            file.createNewFile();

            objectMapper.writeValue(file, new HashMap<String, Object>() {{
                put("albums", new ArrayList<>());
            }});
        }
        List<Album> albums = getAllAlbums();
        if (album.getId() == null || album.getId().isEmpty()) {
            album.setId("album-" + (albums.size() + 1));
        }
        albums.add(album);
        objectMapper.writeValue(file, new HashMap<String, Object>() {{
            put("albums", albums);
        }});

        return album;
    }

    @Override
    public boolean updateAlbumById(String id, Album updatedAlbum) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return false;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return false;
        }

        List<Album> albums = objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );

        boolean found = false;

        for (Album album : albums) {
            if (album.getId().equals(id)) {
                album.setName(updatedAlbum.getName());
                album.setUri(updatedAlbum.getUri());
                album.setReleaseDate(updatedAlbum.getReleaseDate());
                album.setTotalTracks(updatedAlbum.getTotalTracks());
                album.setImages(updatedAlbum.getImages());
                album.setTracks(updatedAlbum.getTracks());
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        ObjectNode updatedRootNode = objectMapper.createObjectNode();
        updatedRootNode.set("albums", objectMapper.valueToTree(albums));

        objectMapper.writeValue(file, updatedRootNode);

        return true;
    }

    @Override
    public boolean deleteAlbumById(String id) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return false;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return false;
        }

        List<Album> albums = objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );

        boolean removed = albums.removeIf(album -> album.getId().equals(id));

        if (!removed) {
            return false;
        }

        ObjectNode updatedRootNode = objectMapper.createObjectNode();
        updatedRootNode.set("albums", objectMapper.valueToTree(albums));

        objectMapper.writeValue(file, updatedRootNode);

        return true;
    }


    //tracks
    @Override
    public List<Track> getTracksByAlbumId(String id) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return null;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return null;
        }

        for (JsonNode albumNode : albumsNode) {
            if (albumNode.get("id").asText().equals(id)) {
                JsonNode tracksNode = albumNode.get("tracks");
                if (tracksNode == null || !tracksNode.isArray()) {
                    return new ArrayList<>();
                }

                return objectMapper.readValue(
                        objectMapper.treeAsTokens(tracksNode),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Track.class)
                );
            }
        }

        return null;
    }

    @Override
    public Album addTrack(String albumId, Track track) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return null;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return null;
        }

        List<Album> albums = objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );

        Album albumToUpdate = null;

        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                if (album.getTracks() == null) {
                    album.setTracks(new ArrayList<>());
                }

                album.getTracks().add(track);
                albumToUpdate = album;
                break;
            }
        }

        if (albumToUpdate == null) {
            return null;  // Album not found
        }

        ObjectNode updatedRootNode = objectMapper.createObjectNode();
        updatedRootNode.set("albums", objectMapper.valueToTree(albums));

        objectMapper.writeValue(file, updatedRootNode);

        return albumToUpdate;  // Return updated album
    }


    @Override
    public Album updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return null;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return null;
        }

        List<Album> albums = objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );

        Album albumToUpdate = null;
        Track trackToUpdate = null;

        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                if (album.getTracks() == null) {
                    return null; // No tracks exist in this album
                }

                for (Track track : album.getTracks()) {
                    if (track.getId().equals(trackId)) {
                        track.setName(updatedTrack.getName());
                        track.setDurationMs(updatedTrack.getDurationMs());
                        track.setExplicit(updatedTrack.isExplicit());
                        track.setUri(updatedTrack.getUri());
                        trackToUpdate = track;
                        break;
                    }
                }

                if (trackToUpdate == null) {
                    return null; // Track not found
                }

                albumToUpdate = album;
                break;
            }
        }

        if (albumToUpdate == null) {
            return null; // Album not found
        }

        ObjectNode updatedRootNode = objectMapper.createObjectNode();
        updatedRootNode.set("albums", objectMapper.valueToTree(albums));

        objectMapper.writeValue(file, updatedRootNode);

        return albumToUpdate;
    }

    @Override
    public boolean deleteTrack(String albumId, String trackId) throws IOException {
        File file = new File("src/data/albums.json");

        if (!file.exists()) {
            return false;
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode albumsNode = rootNode.get("albums");

        if (albumsNode == null || !albumsNode.isArray()) {
            return false;
        }

        List<Album> albums = objectMapper.readValue(
                objectMapper.treeAsTokens(albumsNode),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Album.class)
        );

        Album albumToUpdate = null;
        boolean trackRemoved = false;

        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                if (album.getTracks() == null) {
                    return false; // No tracks exist in this album
                }

                trackRemoved = album.getTracks().removeIf(track -> track.getId().equals(trackId));

                if (!trackRemoved) {
                    return false; // Track not found
                }

                albumToUpdate = album;
                break;
            }
        }

        if (!trackRemoved) {
            return false; // Track not found
        }

        ObjectNode updatedRootNode = objectMapper.createObjectNode();
        updatedRootNode.set("albums", objectMapper.valueToTree(albums));

        objectMapper.writeValue(file, updatedRootNode);

        return true;
    }

    //artiest
    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData("src/data/popular_artists.json");
        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return null;
        }
        return objectMapper.treeToValue(artistNode, Artist.class);
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        JsonNode artistsNode = loadJsonData("src/data/popular_artists.json");

        if (artistsNode == null || !artistsNode.isObject()) {
            return new ArrayList<>(); // Return empty list if invalid format
        }

        List<Artist> artists = new ArrayList<>();
        for (JsonNode artistNode : artistsNode) {
            Artist artist = objectMapper.treeToValue(artistNode, Artist.class);
            artists.add(artist);
        }

        return artists; // Return list of artists
    }


    @Override
    public Artist saveArtist(Artist artist) throws IOException {
        File file = new File("src/data/popular_artists.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new HashMap<>()); // Create empty JSON file
        }

        JsonNode rootNode = objectMapper.readTree(file);
        if (rootNode == null || !rootNode.isObject()) {
            rootNode = objectMapper.createObjectNode(); // Ensure valid format
        }

        ObjectNode artistsNode = (ObjectNode) rootNode;

        // Generate unique artist ID if missing
        if (artist.getId() == null || artist.getId().isEmpty()) {
            artist.setId("artist-" + System.currentTimeMillis()); // Unique ID
        }

        // Convert artist to JSON
        artistsNode.set(artist.getId(), objectMapper.valueToTree(artist));

        // Save to file
        objectMapper.writeValue(file, artistsNode);

        return artist;
    }

    @Override
    public Artist updateArtist(String id, Artist updatedArtist) throws IOException {
        File file = new File("src/data/popular_artists.json");

        if (!file.exists()) {
            return null; // File does not exist
        }

        JsonNode rootNode = objectMapper.readTree(file);
        if (rootNode == null || !rootNode.isObject()) {
            return null; // Invalid JSON format
        }

        ObjectNode artistsNode = (ObjectNode) rootNode;

        // Check if artist exists
        JsonNode existingArtistNode = artistsNode.get(id);
        if (existingArtistNode == null) {
            return null; // Artist not found
        }

        // Update artist fields
        Artist existingArtist = objectMapper.treeToValue(existingArtistNode, Artist.class);
        existingArtist.setName(updatedArtist.getName());
        existingArtist.setFollowers(updatedArtist.getFollowers());
        existingArtist.setGenres(updatedArtist.getGenres());
        existingArtist.setPopularity(updatedArtist.getPopularity());
        existingArtist.setUri(updatedArtist.getUri());

        // Save updated artist back to JSON
        artistsNode.set(id, objectMapper.valueToTree(existingArtist));
        objectMapper.writeValue(file, artistsNode);

        return existingArtist;
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {
        File file = new File("src/data/popular_artists.json");

        if (!file.exists()) {
            return false; // File does not exist
        }

        JsonNode rootNode = objectMapper.readTree(file);
        if (rootNode == null || !rootNode.isObject()) {
            return false; // Invalid JSON format
        }

        ObjectNode artistsNode = (ObjectNode) rootNode;

        // Check if artist exists
        if (!artistsNode.has(id)) {
            return false; // Artist not found
        }

        // Remove artist from JSON
        artistsNode.remove(id);
        objectMapper.writeValue(file, artistsNode);

        return true; // Artist deleted successfully
    }

    @Override
    public List<Album> getAlbumsByArtistId(String id) throws IOException {
        return List.of();
    }

    //songs
    @Override
    public List<Song> getAllSongs() throws IOException {
        JsonNode rootNode = loadJsonData("src/data/popular_songs.json");

        List<Song> songs = new ArrayList<>();
        if (rootNode.isArray()) {
            for (JsonNode songNode : rootNode) {
                Song song = objectMapper.treeToValue(songNode, Song.class);
                songs.add(song);
            }
        } else {
            throw new IOException("Invalid JSON format: Expected an array of songs");
        }

        return songs;
    }

    @Override
    public Song getSongById(String id) throws IOException {
        List<Song> songs = getAllSongs();
        return songs.stream()
                .filter(song -> song.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Song saveSong(Song song) throws IOException {
        File file = new File("src/data/popular_songs.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<>());
        }

        List<Song> songs = getAllSongs();

        if (song.getId() == null || song.getId().isEmpty()) {
            song.setId("song-" + (songs.size() + 1));
        }

        songs.add(song);
        objectMapper.writeValue(file, songs);

        return song;
    }

    @Override
    public Song updateSong(String id, Song updatedSong) throws IOException {
        // Define the file path
        File file = new File("src/data/popular_songs.json");

        if (!file.exists()) {
            return null; // File does not exist, return null
        }

        // Read the JSON file
        List<Song> songs = getAllSongs(); // Ensure this method reads from "popular_songs.json"
        boolean isUpdated = false;

        // Update the song in the list
        for (Song song : songs) {
            if (song.getId().equals(id)) {
                song.setName(updatedSong.getName());
                song.setPopularity(updatedSong.getPopularity());
                song.setUri(updatedSong.getUri());
                song.setDurationMs(updatedSong.getDurationMs());
                song.setAlbum(updatedSong.getAlbum());
                song.setArtists(updatedSong.getArtists());
                isUpdated = true;
                break;
            }
        }

        // If no song was found, return null
        if (!isUpdated) {
            return null;
        }

        // Write the updated list back to the same file
        objectMapper.writeValue(file, songs);

        // Return the updated song
        return updatedSong;
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        File file = new File("src/data/popular_songs.json");

        if (!file.exists()) {
            return false;
        }

        List<Song> songs = getAllSongs();
        boolean removed = songs.removeIf(song -> song.getId().equals(id));

        if (!removed) {
            return false;
        }

        objectMapper.writeValue(file, songs);
        return true;
    }




    private JsonNode loadJsonData(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();

            objectMapper.writeValue(file, new HashMap<String, Object>() {{
                if (filePath.contains("albums")) {
                    put("albums", new ArrayList<>());
                } else if (filePath.contains("popular_artists")) {
                    put("artists", new ArrayList<>());
                } else if (filePath.contains("popular_songs")) {
                    put("songs", new ArrayList<>());
                }
            }});

            return objectMapper.createArrayNode(); // Return an empty JSON array
        }

        JsonNode jsonNode = objectMapper.readTree(file);

        return jsonNode;
    }








}