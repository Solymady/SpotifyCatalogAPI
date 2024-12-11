package com.example.catalog;

import com.example.catalog.utils.SpotifyUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class SpotifyUtilsTest {

    @Test
    public void testValidId() {
        assertTrue(isValidId("6rqhFgbbKwnb9MLmUQDhG6")); // valid Spotify ID
        assertTrue(isValidId("1a2B3c4D5e6F7g8H9iJkL0mN")); // valid 22 character ID
        assertTrue(isValidId("a1b2C3d4E5f6G7h8I9jK0L1m2N")); // valid 30 character ID
    }

    @Test
    public void testInvalidId() {
        assertFalse(isValidId(null)); // null ID
        assertFalse(isValidId("")); // empty ID
        assertFalse(isValidId("shortID")); // too short ID (less than 15 characters)
        assertFalse(isValidId("thisIDiswaytoolongtobevalidfduaf")); // too long ID (more than 30 characters)
        assertFalse(isValidId("!@#$$%^&*()_+")); // invalid characters
        assertFalse(isValidId("1234567890abcdefGHIJKLMNO!@#")); // includes invalid characters
    }

    @Test
    public void testValidURI() {
        assertTrue(isValidURI("spotify:track:4iV5W9uYEdYUVa79Axb7Rh")); // valid URI
        assertTrue(isValidURI("spotify:album:1DFixLWuPkv3KT3TnV35m3")); // valid album URI
        assertTrue(isValidURI("spotify:artist:6rqhFgbbKwnb9MLmUQDhG6")); // valid artist URI
        assertTrue(isValidURI("spotify:playlist:37i9dQZF1DXcBWIGoYBM5M")); // valid playlist URI
    }

    @Test
    public void testInvalidURI() {
        assertFalse(isValidURI(null)); // null URI
        assertFalse(isValidURI("")); // empty string
        assertFalse(isValidURI("spotify:track:")); // missing ID
        assertFalse(isValidURI("spotify:invalid:4iV5W9uYEdYUVa79Axb7Rh")); // invalid resource type
        assertFalse(isValidURI("http://example.com")); // invalid format
        assertFalse(isValidURI("spotify:track:invalidID")); // invalid ID
    }

    @Test
    public void testGetSpotifyClient_InvalidInputs() {
        // Test null client ID
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient(null, "validSecret");
        }, "Should throw exception when client ID is null");

        // Test empty client ID
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("", "validSecret");
        }, "Should throw exception when client ID is empty");

        // Test null client secret
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("validId", null);
        }, "Should throw exception when client secret is null");

        // Test empty client secret
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("validId", "");
        }, "Should throw exception when client secret is empty");

        // Test valid input (should throw UnsupportedOperationException)
        assertThrows(UnsupportedOperationException.class, () -> {
            SpotifyUtils.getSpotifyClient("validClientId", "validClientSecret");
        }, "Should throw UnsupportedOperationException for valid input");
    }


}
