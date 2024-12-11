package com.example.catalog;

import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        // Sample song data for testing. TODO - Add more songs
        String jsonData = """
                    [
                        {
                          "duration_ms": 200040,
                          "name": "Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 245213,
                          "name": "Shape of You",
                          "popularity": 91,
                          "album": {
                            "name": "÷ (Divide)",
                            "release_date": "2017-03-03",
                            "total_tracks": 16
                          },
                          "artists": [
                            {
                              "name": "Ed Sheeran"
                            }
                          ]
                        },
                        {
                          "duration_ms": 182732,
                          "name": "Levitating",
                          "popularity": 88,
                          "album": {
                            "name": "Future Nostalgia",
                            "release_date": "2020-03-27",
                            "total_tracks": 11
                          },
                          "artists": [
                            {
                              "name": "Dua Lipa"
                            }
                          ]
                        },
                        {
                          "duration_ms": 216000,
                          "name": "Uptown Funk",
                          "popularity": 90,
                          "album": {
                            "name": "Uptown Special",
                            "release_date": "2015-01-13",
                            "total_tracks": 11
                          },
                          "artists": [
                            {
                              "name": "Mark Ronson"
                            },
                            {
                              "name": "Bruno Mars"
                            }
                          ]
                        },
                        {
                          "duration_ms": 211840,
                          "name": "Bad Guy",
                          "popularity": 85,
                          "album": {
                            "name": "WHEN WE ALL FALL ASLEEP, WHERE DO WE GO?",
                            "release_date": "2019-03-29",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "Billie Eilish"
                            }
                          ]
                        },
                        {
                          "duration_ms": 190000,
                          "name": "Rolling in the Deep",
                          "popularity": 89,
                          "album": {
                            "name": "21",
                            "release_date": "2011-01-24",
                            "total_tracks": 12
                          },
                          "artists": [
                            {
                              "name": "Adele"
                            }
                          ]
                        },
                        {
                          "duration_ms": 365000,
                          "name": "Hello",
                          "popularity": 92,
                          "album": {
                            "name": "25",
                            "release_date": "2015-11-20",
                            "total_tracks": 11
                          },
                          "artists": [
                            {
                              "name": "Adele"
                            }
                          ]
                        },
                        {
                          "duration_ms": 180000,
                          "name": "Unknown Melody",
                          "popularity": 0,
                          "album": {
                            "name": "Hidden Gems",
                            "release_date": "2023-11-15",
                            "total_tracks": 10
                          },
                          "artists": [
                            {
                              "name": "Anonymous Artist"
                            }
                          ]
                        },
                        {
                          "duration_ms": 210000,
                          "name": "Global Hit",
                          "popularity": 100,
                          "album": {
                            "name": "Worldwide Anthems",
                            "release_date": "2024-01-01",
                            "total_tracks": 12
                          },
                          "artists": [
                            {
                              "name": "Superstar Artist"
                            }
                          ]
                        }
                    ]
                """;
        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
    }

    @Test
    void sortSongsByNameTest() {

        List<JsonNode> sortedSongs = catalogUtils.sortSongsByName(songs);

        assertEquals("Bad Guy",sortedSongs.get(0).get("name").asText());
        assertEquals("Blinding Lights",sortedSongs.get(1).get("name").asText());
        assertEquals("Global Hit",sortedSongs.get(2).get("name").asText());
        assertEquals("Hello",sortedSongs.get(3).get("name").asText());
        assertEquals("Levitating",sortedSongs.get(4).get("name").asText());
        assertEquals("Rolling in the Deep",sortedSongs.get(5).get("name").asText());
        assertEquals("Shape of You",sortedSongs.get(6).get("name").asText());
        assertEquals("Unknown Melody",sortedSongs.get(7).get("name").asText());
        assertEquals("Uptown Funk",sortedSongs.get(8).get("name").asText());

    }

    @Test
    void filterSongsByPopularityTest(){

        List<JsonNode> filteredSongs = catalogUtils.filterSongsByPopularity(songs,90);

        assertTrue(filteredSongs.stream().anyMatch(songs ->songs.get("name").asText().equals("Shape of You")));
        assertTrue(filteredSongs.stream().anyMatch(songs ->songs.get("name").asText().equals("Uptown Funk")));
        assertTrue(filteredSongs.stream().anyMatch(songs ->songs.get("name").asText().equals("Global Hit")));
        assertTrue(filteredSongs.stream().anyMatch(songs ->songs.get("name").asText().equals("Hello")));
    }

    @Test
    void doesSongExistByNameTest(){

        boolean songByName1 = catalogUtils.doesSongExistByName(songs,"Shape of You");
        assertTrue(songByName1);
        boolean songByName2= catalogUtils.doesSongExistByName(songs,"shape of you");
        assertTrue(songByName2);
        boolean songByName3= catalogUtils.doesSongExistByName(songs,"Shape of you");
        assertTrue(songByName3);

    }

    @Test
    void countSongsByArtistTest(){

        long count0 = catalogUtils.countSongsByArtist(songs,"solyma");
        assertEquals(0,count0);
        long count1 = catalogUtils.countSongsByArtist(songs,"Anonymous Artist");
        assertEquals(1,count1);
        long count2 = catalogUtils.countSongsByArtist(songs,"Adele");
        assertEquals(2,count2);
        long count3 = catalogUtils.countSongsByArtist(songs,"adele");
        assertEquals(2,count3);
    }

    @Test
    void getLongestSongTest(){
        JsonNode longestSong = catalogUtils.getLongestSong(songs);
        assertEquals("Hello",longestSong.get("name").asText());
        // assertEquals("hello",longestSong.get("name").asText());
    }

    @Test
    void getSongByYearTest(){
        List<JsonNode> filteredSongs1=catalogUtils.getSongByYear(songs,2015);
        assertEquals(2,filteredSongs1.size());

        List<JsonNode> filteredSongs2=catalogUtils.getSongByYear(songs,2023);
        assertEquals(1,filteredSongs2.size());

        List<JsonNode> filteredSongs3=catalogUtils.getSongByYear(songs,1900);
        assertEquals(0,filteredSongs3.size());
    }

    @Test
    void getMostRecentSongTest(){
        JsonNode mostRecentSong = catalogUtils.getMostRecentSong(songs);
        //System.out.println(mostRecentSong.get("album").get("release_date").asText());
        assertEquals("2024-01-01",mostRecentSong.get("album").get("release_date").asText());
    }


}