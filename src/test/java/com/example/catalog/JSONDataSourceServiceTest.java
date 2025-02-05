package com.example.catalog;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONDataSourceServiceTest {

    private JSONDataSourceService jsonService;

    @BeforeEach
    void setUp() throws IOException {
        jsonService = new JSONDataSourceService();
    }

    @Test
    void testGetAllAlbums() throws IOException {
        List<Album> albums = jsonService.getAllAlbums();
        assertNotNull(albums);
        System.out.println("Albums retrieved: " + albums.size());
    }

    @Test
    void testSaveAndGetAlbum() throws IOException {
        Album album = new Album();
        album.setName("Test Album");
        album.setReleaseDate("2025-01-01");

        Album savedAlbum = jsonService.saveAlbum(album);
        assertNotNull(savedAlbum);
        assertNotNull(savedAlbum.getId());

        Album retrievedAlbum = jsonService.getAlbumById(savedAlbum.getId());
        assertNotNull(retrievedAlbum);
        assertEquals("Test Album", retrievedAlbum.getName());
    }

    @Test
    void testUpdateAlbum() throws IOException {
        Album album = new Album();
        album.setName("Old Album");
        album.setReleaseDate("2024-01-01");
        Album savedAlbum = jsonService.saveAlbum(album);

        Album updatedAlbum = new Album();
        updatedAlbum.setName("Updated Album");
        updatedAlbum.setReleaseDate("2025-01-01");

        boolean success = jsonService.updateAlbumById(savedAlbum.getId(), updatedAlbum);
        assertTrue(success);

        Album retrievedAlbum = jsonService.getAlbumById(savedAlbum.getId());
        assertEquals("Updated Album", retrievedAlbum.getName());
    }

    @Test
    void testDeleteAlbum() throws IOException {
        Album album = new Album();
        album.setName("To be Deleted");
        Album savedAlbum = jsonService.saveAlbum(album);

        boolean deleted = jsonService.deleteAlbumById(savedAlbum.getId());
        assertTrue(deleted);

        Album retrievedAlbum = jsonService.getAlbumById(savedAlbum.getId());
        assertNull(retrievedAlbum);
    }

    @Test
    void testGetTracksByAlbumId() throws IOException {
        Album album = new Album();
        album.setName("Album with Tracks");
        Album savedAlbum = jsonService.saveAlbum(album);

        Track track1 = new Track();
        track1.setName("Track One");

        Track track2 = new Track();
        track2.setName("Track Two");

        jsonService.addTrack(savedAlbum.getId(), track1);
        jsonService.addTrack(savedAlbum.getId(), track2);

        List<Track> tracks = jsonService.getTracksByAlbumId(savedAlbum.getId());
        assertNotNull(tracks);
        assertEquals(2, tracks.size());
    }

    @Test
    void testAddTrack() throws IOException {
        Album album = new Album();
        album.setName("Album for Track");
        Album savedAlbum = jsonService.saveAlbum(album);

        Track track = new Track();
        track.setName("New Track");

        Album updatedAlbum = jsonService.addTrack(savedAlbum.getId(), track);
        assertNotNull(updatedAlbum);
        assertEquals(1, updatedAlbum.getTracks().size());
    }

    @Test
    void testGetArtistById() throws IOException {
        Artist artist = new Artist();
        artist.setName("Test Artist");
        Artist savedArtist = jsonService.saveArtist(artist);

        Artist retrievedArtist = jsonService.getArtistById(savedArtist.getId());
        assertNotNull(retrievedArtist);
        assertEquals("Test Artist", retrievedArtist.getName());
    }

    @Test
    void testGetAllArtists() throws IOException {
        List<Artist> artists = jsonService.getAllArtists();
        assertNotNull(artists);
        System.out.println("Artists retrieved: " + artists.size());
    }

    @Test
    void testSaveArtist() throws IOException {
        Artist artist = new Artist();
        artist.setName("New Artist");
        Artist savedArtist = jsonService.saveArtist(artist);
        assertNotNull(savedArtist);
        assertNotNull(savedArtist.getId());
    }

    @Test
    void testUpdateArtist() throws IOException {
        Artist artist = new Artist();
        artist.setName("Old Artist");
        Artist savedArtist = jsonService.saveArtist(artist);

        Artist updatedArtist = new Artist();
        updatedArtist.setName("Updated Artist");

        Artist result = jsonService.updateArtist(savedArtist.getId(), updatedArtist);
        assertNotNull(result);
        assertEquals("Updated Artist", result.getName());
    }

    // ✅ Test: Delete Artist
    @Test
    void testDeleteArtist() throws IOException {
        Artist artist = new Artist();
        artist.setName("Artist to Delete");
        Artist savedArtist = jsonService.saveArtist(artist);

        boolean deleted = jsonService.deleteArtist(savedArtist.getId());
        assertTrue(deleted);

        Artist retrievedArtist = jsonService.getArtistById(savedArtist.getId());
        assertNull(retrievedArtist);
    }


    @Test
    void testGetSongById() throws IOException {
        Song song = new Song();
        song.setName("Test Song");
        Song savedSong = jsonService.saveSong(song);

        Song retrievedSong = jsonService.getSongById(savedSong.getId());
        assertNotNull(retrievedSong);
        assertEquals("Test Song", retrievedSong.getName());
    }

    @Test
    void testSaveSong() throws IOException {
        Song song = new Song();
        song.setName("New Song");
        Song savedSong = jsonService.saveSong(song);
        assertNotNull(savedSong);
        assertNotNull(savedSong.getId());
    }

    @Test
    void testUpdateSong() throws IOException {
        Song song = new Song();
        song.setName("Old Song");
        Song savedSong = jsonService.saveSong(song);

        Song updatedSong = new Song();
        updatedSong.setName("Updated Song");

        Song result = jsonService.updateSong(savedSong.getId(), updatedSong);
        assertNotNull(result);
        assertEquals("Updated Song", result.getName());
    }

    @Test
    void testDeleteSong() throws IOException {
        Song song = new Song();
        song.setName("Song to Delete");
        Song savedSong = jsonService.saveSong(song);

        boolean deleted = jsonService.deleteSong(savedSong.getId());
        assertTrue(deleted);

        Song retrievedSong = jsonService.getSongById(savedSong.getId());
        assertNull(retrievedSong);
    }
}
