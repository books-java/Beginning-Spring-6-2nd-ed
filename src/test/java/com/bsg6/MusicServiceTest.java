package com.bsg6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.bsg6.service.MusicService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(locations = "/config.xml")
public class MusicServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ApplicationContext context;
    @Autowired
    MusicServiceTestHelper tests;

    @Autowired
    MusicService service;

    @Test
    public void testConfiguration() {
        assertNotNull(context);
        Set<String> definitions = new HashSet<>(
                Arrays.asList(context.getBeanDefinitionNames()));

        /**/
        // uncomment if you'd like to see the entire set of defined beans
        for (String d : definitions) {
            System.out.println(d);
        }

        assertTrue(definitions.contains("musicServiceImpl"));
    }

    /*
     * @Test
     * public void testMusicService() {
     * Song song = service.getSong(
     * "Threadbare Loaf", "Someone Stole the Flour");
     * assertEquals(song.getVotes(), 0);
     * }
     */

    @Test
    public void testSongVoting() {
        tests.testSongVoting(service);
    }

    @Test
    public void testGetMatchingArtistNames() {
        tests.testMatchingArtistNames(service);
    }

    @Test
    public void testGetSongsForArtist() {
        tests.testSongsForArtist(service);
    }

    @Test
    public void testMatchingSongNamesForArtist() {
        tests.testMatchingSongNamesForArtist(service);
    }
}