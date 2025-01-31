package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;

import java.io.IOException;
import java.util.List;

public interface DataSourceService {

    //albums
    List<Album> getAllAlbums() throws IOException;
    Album getAlbumById(String id) throws IOException;
    Album saveAlbum(Album album) throws IOException;
    boolean updateAlbumById(String id, Album updatedAlbum) throws IOException;
    boolean deleteAlbumById(String id) throws IOException;
    List<Track> getTracksByAlbumId(String id) throws IOException;
    Album addTrack(String id, Track track) throws IOException;
    Album updateTrack(String id, String trackId, Track updatedTrack) throws IOException;
    boolean deleteTrack(String id, String trackId) throws IOException;

    //artiest
    List<Artist> getAllArtists() throws IOException;
    Artist getArtistById(String id) throws IOException;
    Artist saveArtist(Artist artist) throws IOException;
    Artist updateArtist(String id, Artist updatedArtist) throws IOException;
    boolean deleteArtist(String id) throws IOException;

    List<Album> getAlbumsByArtistId(String id) throws IOException;

    //songs
    List<Song> getAllSongs() throws IOException;
    Song getSongById(String id) throws IOException;
    Song saveSong(Song song) throws IOException;
    Song updateSong(String id, Song updatedSong) throws IOException;
    boolean deleteSong(String id) throws IOException;


    /**

    List<Album> getAllAlbums() throws IOException;

    Song getSongById(String id) throws IOException;
    List<Song> getAllSongs() throws IOException;

    void createArtist(Artist artist) throws IOException;
    void updateArtist(String id, Artist artist) throws IOException;
    void deleteArtist(String id) throws IOException;**/
}