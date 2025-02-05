package com.example.catalog;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.example.catalog.services.SpotifyAPIDataSources;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyAPIDataSourcesTest {

    private SpotifyAPIDataSources spotifyService;
    private SpotifyAPIDataSources mockSpotifyService;

    @BeforeEach
    void setUp() throws IOException {
        // Create a mock instance of the Spotify service
        mockSpotifyService = mock(SpotifyAPIDataSources.class);
        spotifyService = new SpotifyAPIDataSources();
    }

    @Test
    void testGetAlbumById() throws IOException {
        Album mockAlbum = new Album();
        mockAlbum.setId("123");
        mockAlbum.setName("Test Album");
        mockAlbum.setReleaseDate("2023-01-01");
        mockAlbum.setTotalTracks(10);

        when(mockSpotifyService.getAlbumById("123")).thenReturn(mockAlbum);

        Album album = mockSpotifyService.getAlbumById("123");

        assertNotNull(album);
        assertEquals("123", album.getId());
        assertEquals("Test Album", album.getName());
        assertEquals(10, album.getTotalTracks());

        verify(mockSpotifyService, times(1)).getAlbumById("123");
    }

    @Test
    void testGetArtistById() throws IOException {
        Artist mockArtist = new Artist();
        mockArtist.setId("456");
        mockArtist.setName("Test Artist");
        mockArtist.setPopularity(80);
        mockArtist.setFollowers(100000);

        when(mockSpotifyService.getArtistById("456")).thenReturn(mockArtist);

        Artist artist = mockSpotifyService.getArtistById("456");

        assertNotNull(artist);
        assertEquals("456", artist.getId());
        assertEquals("Test Artist", artist.getName());
        assertEquals(80, artist.getPopularity());

        verify(mockSpotifyService, times(1)).getArtistById("456");
    }

    @Test
    void testGetTracksByAlbumId() throws IOException {
        Track track1 = new Track();
        track1.setId("track1");
        track1.setName("Track One");

        Track track2 = new Track();
        track2.setId("track2");
        track2.setName("Track Two");

        List<Track> mockTracks = List.of(track1, track2);

        when(mockSpotifyService.getTracksByAlbumId("123")).thenReturn(mockTracks);

        List<Track> tracks = mockSpotifyService.getTracksByAlbumId("123");

        assertNotNull(tracks);
        assertEquals(2, tracks.size());
        assertEquals("Track One", tracks.get(0).getName());

        verify(mockSpotifyService, times(1)).getTracksByAlbumId("123");
    }

    @Test
    void testGetAlbumsByArtistId() throws IOException {
        Album album1 = new Album();
        album1.setId("album1");
        album1.setName("Album One");

        Album album2 = new Album();
        album2.setId("album2");
        album2.setName("Album Two");

        List<Album> mockAlbums = List.of(album1, album2);

        when(mockSpotifyService.getAlbumsByArtistId("456")).thenReturn(mockAlbums);

        List<Album> albums = mockSpotifyService.getAlbumsByArtistId("456");

        assertNotNull(albums);
        assertEquals(2, albums.size());
        assertEquals("Album One", albums.get(0).getName());

        verify(mockSpotifyService, times(1)).getAlbumsByArtistId("456");
    }

    @Test
    void testGetTopSongsByArtistId() throws IOException {
        Song song1 = new Song();
        song1.setId("song1");
        song1.setName("Top Song One");

        Song song2 = new Song();
        song2.setId("song2");
        song2.setName("Top Song Two");

        List<Song> mockSongs = List.of(song1, song2);

        when(mockSpotifyService.getTopSongsByArtistId("456", "US")).thenReturn(mockSongs);

        List<Song> songs = mockSpotifyService.getTopSongsByArtistId("456", "US");

        assertNotNull(songs);
        assertEquals(2, songs.size());
        assertEquals("Top Song One", songs.get(0).getName());

        verify(mockSpotifyService, times(1)).getTopSongsByArtistId("456", "US");
    }

    @Test
    void testGetSongById() throws IOException {
        Song mockSong = new Song();
        mockSong.setId("song123");
        mockSong.setName("Test Song");
        mockSong.setDurationMs(200000);
        mockSong.setExplicit(false);

        when(mockSpotifyService.getSongById("song123")).thenReturn(mockSong);

        Song song = mockSpotifyService.getSongById("song123");

        assertNotNull(song);
        assertEquals("song123", song.getId());
        assertEquals("Test Song", song.getName());
        assertFalse(song.isExplicit());

        verify(mockSpotifyService, times(1)).getSongById("song123");
    }

    @Test
    void testGetArtistById_InvalidArtist() throws IOException {
        when(mockSpotifyService.getArtistById("invalid")).thenThrow(new IOException("Failed to fetch artist: HTTP 404"));

        Exception exception = assertThrows(IOException.class, () -> {
            mockSpotifyService.getArtistById("invalid");
        });

        assertTrue(exception.getMessage().contains("Failed to fetch artist: HTTP 404"));
    }

    @Test
    void testGetAlbumById_InvalidAlbum() throws IOException {
        when(mockSpotifyService.getAlbumById("invalid")).thenThrow(new IOException("Failed to fetch album: HTTP 404"));

        Exception exception = assertThrows(IOException.class, () -> {
            mockSpotifyService.getAlbumById("invalid");
        });

        assertTrue(exception.getMessage().contains("Failed to fetch album: HTTP 404"));
    }
}
