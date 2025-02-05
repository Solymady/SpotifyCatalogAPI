package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class SpotifyAPIDataSources implements DataSourceService {

    private static final String CLIENT_ID = "b05ec9fb6dd74bd48deaebc708b76078"; // Replace with your Client ID
    private static final String CLIENT_SECRET = "32bdaf4368bc4d15b24731b0643178fe"; // Replace with your Client Secret
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String BASE_URL = "https://api.spotify.com/v1/albums/";


    private String accessToken;

    public SpotifyAPIDataSources() throws IOException {
        this.accessToken = getAccessToken();
    }
    private static String getAccessToken() throws IOException {
        URL url = new URL(TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodeCredentials());
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String requestBody = "grant_type=client_credentials";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to retrieve access token: " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String token = jsonNode.get("access_token").asText();

        return token;
    }


    // Encode Client Credentials
    private static String encodeCredentials() {
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public List<Album> getAllAlbums() throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Album getAlbumById(String albumId) throws IOException {
        URL url = new URL(BASE_URL + albumId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch album: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode albumNode = objectMapper.readTree(responseBody);

        Album album = new Album();
        album.setId(albumNode.get("id").asText());
        album.setName(albumNode.get("name").asText());
        album.setReleaseDate(albumNode.get("release_date").asText());
        album.setTotalTracks(albumNode.get("total_tracks").asInt());

        return album;
    }

    // Save Album
    @Override
    public Album saveAlbum(Album album) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public boolean updateAlbumById(String id, Album updatedAlbum) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public boolean deleteAlbumById(String id) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public List<Track> getTracksByAlbumId(String albumId) throws IOException {
        URL url = new URL(BASE_URL + albumId + "/tracks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch tracks: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tracksNode = objectMapper.readTree(responseBody).get("items");
        List<Track> tracks = new ArrayList<>();

        for (JsonNode trackNode : tracksNode) {
            Track track = new Track();
            track.setId(trackNode.get("id").asText());
            track.setName(trackNode.get("name").asText());
            tracks.add(track);
        }

        return tracks;
    }

    @Override
    public Album addTrack(String id, Track track) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Album updateTrack(String id, String trackId, Track updatedTrack) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public boolean deleteTrack(String id, String trackId) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Artist getArtistById(String artistId) throws IOException {
        URL url = new URL("https://api.spotify.com/v1/artists/" + artistId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch artist: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode artistNode = objectMapper.readTree(responseBody);

        Artist artist = new Artist();
        artist.setId(artistNode.get("id").asText());
        artist.setName(artistNode.get("name").asText());
        artist.setGenres(objectMapper.convertValue(artistNode.get("genres"), List.class));
        artist.setPopularity(artistNode.get("popularity").asInt());
        artist.setFollowers(artistNode.get("followers").get("total").asInt());

        return artist;
    }


    @Override
    public Artist saveArtist(Artist artist) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Artist updateArtist(String id, Artist updatedArtist) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public List<Album> getAlbumsByArtistId(String artistId) throws IOException {
        URL url = new URL("https://api.spotify.com/v1/artists/" + artistId + "/albums");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch albums: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode albumsNode = objectMapper.readTree(responseBody).get("items");
        List<Album> albums = new ArrayList<>();

        for (JsonNode albumNode : albumsNode) {
            Album album = new Album();
            album.setId(albumNode.get("id").asText());
            album.setName(albumNode.get("name").asText());
            album.setReleaseDate(albumNode.get("release_date").asText());
            album.setTotalTracks(albumNode.get("total_tracks").asInt());
            albums.add(album);
        }

        return albums;
    }

    @Override
    public List<Song> getTopSongsByArtistId(String artistId, String market) throws IOException {
        URL url = new URL("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?market=" + market);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch top songs: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode songsNode = objectMapper.readTree(responseBody).get("tracks");
        List<Song> topSongs = new ArrayList<>();

        for (JsonNode songNode : songsNode) {
            Song song = new Song();
            song.setId(songNode.get("id").asText());
            song.setName(songNode.get("name").asText());
            song.setDurationMs(songNode.get("duration_ms").asInt());
            song.setPopularity(songNode.get("popularity").asInt());
            topSongs.add(song);
        }

        return topSongs;
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Song getSongById(String songId) throws IOException {
        URL url = new URL("https://api.spotify.com/v1/tracks/" + songId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch song: HTTP " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode songNode = objectMapper.readTree(responseBody);

        Song song = new Song();
        song.setId(songNode.get("id").asText());
        song.setName(songNode.get("name").asText());
        song.setDurationMs(songNode.get("duration_ms").asInt());
        song.setExplicit(songNode.get("explicit").asBoolean());

        return song;
    }

    @Override
    public Song saveSong(Song song) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public Song updateSong(String id, Song updatedSong) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        throw new UnsupportedOperationException("not supported by the Spotify API.");
    }
}
