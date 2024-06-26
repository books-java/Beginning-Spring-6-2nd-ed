package com.bsg6.data.jpa.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bsg6.data.jpa.model.Artist;
import com.bsg6.data.jpa.model.Song;
import com.bsg6.data.jpa.repository.ArtistRepository;
import com.bsg6.data.jpa.repository.SongRepository;

@DataJpaTest
public class MusicServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MusicService musicService;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    SongRepository songRepository;

    private Object[][] model = new Object[][] {
            { "Threadbare Loaf", "Someone Stole the Flour", 4 },
            { "Threadbare Loaf", "What Happened To Our First CD?", 17 },
            { "Therapy Zeppelin", "Medium", 4 },
            { "Clancy in Silt", "Igneous", 5 }
    };

    @BeforeMethod
    public void clearDatabase() {
        songRepository.deleteAll();
        artistRepository.deleteAll();
        populateService();
    }

    protected Integer getNonexistentId() {
        return 1928491;
    }

    void iterateOverModel(Consumer<Object[]> consumer) {
        for (Object[] data : model) {
            consumer.accept(data);
        }
    }

    void populateService() {
        iterateOverModel(data -> {
            for (int i = 0; i < (Integer) data[2]; i++) {
                musicService.voteForSong(
                        data[0].toString(),
                        data[1].toString());
            }
        });
    }

    @Test
    void testSongVoting() {
        iterateOverModel(data -> assertEquals(
                musicService.getSong(
                        data[0].toString(),
                        data[1].toString()).getVotes(),
                ((Integer) data[2]).intValue()));
    }

    @Test
    void testSongsForArtist() {
        List<Song> songs = musicService.getSongsForArtist("Threadbare Loaf");
        assertEquals(songs.size(), 2);
        assertEquals(songs.get(0).getName(),
                "What Happened To Our First CD?");
        assertEquals(songs.get(0).getVotes(), 17);
        assertEquals(songs.get(1).getName(),
                "Someone Stole the Flour");
        assertEquals(songs.get(1).getVotes(), 4);
    }

    @Test
    void testMatchingArtistNames() {
        List<String> names = musicService.getMatchingArtistNames("Th");
        assertEquals(names.size(), 2);
        assertEquals(names.get(0), "Therapy Zeppelin");
        assertEquals(names.get(1), "Threadbare Loaf");
    }

    @Test
    void testFindArtistById() {
        Artist artist = musicService.getArtist("Threadbare Loaf");
        assertNotNull(artist);
        Artist searched = musicService.getArtistById(artist.getId());
        assertNotNull(searched);
        assertEquals(artist.getName(), searched.getName());
        searched = musicService.getArtistById(getNonexistentId());
        assertNull(searched);
    }

    @Test
    void testFindSongById() {
        Song song = musicService.getSong("Therapy Zeppelin",
                "Medium");
        assertNotNull(song);
        Song searched = musicService.getSongById(song.getId());
        assertNotNull(searched);
        assertEquals(song.getName(), searched.getName());
        searched = musicService.getSongById(getNonexistentId());
        assertNull(searched);
    }

    @Test
    void testMatchingSongNamesForArtist() {
        List<String> names = musicService.getMatchingSongNamesForArtist(
                "Threadbare Loaf", "W");
        assertEquals(names.size(), 1);
        assertEquals(names.get(0),
                "What Happened To Our First CD?");
    }

    @DataProvider
    public Object[][] artistSearches() {
        return new Object[][] {
                new Object[] { "", 3 },
                new Object[] { "T", 2 },
                new Object[] { "Th", 2 },
                new Object[] { "Thr", 1 },
                new Object[] { "C", 1 },
                new Object[] { "Z", 0 }
        };
    }

    @Test(dataProvider = "artistSearches")
    public void testSearches(String name, int count) {
        List<String> names = musicService.getMatchingArtistNames(name);
        assertNotNull(names);
        assertEquals(names.size(), count);
    }
}
