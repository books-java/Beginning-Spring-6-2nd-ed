package com.bsg6.data.jpa.repository;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bsg6.data.jpa.model.Artist;
import com.bsg6.data.jpa.model.Song;
import com.bsg6.data.util.WildcardConverter;

@DataJpaTest
public class SongRepositoryTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SongRepository songRepository;
    
    @Autowired
    WildcardConverter converter;

    protected Artist createArtist(String name) {
        return new Artist(name);
    }

    protected Song createSong(Artist artist, String name) {
        return new Song(artist, name);
    }

    @BeforeMethod
    public void clearDatabase() {
        songRepository.deleteAll();
        artistRepository.deleteAll();
        buildModel();
    }

    private Object[][] model = new Object[][] {
            { "Threadbare Loaf", "Someone Stole the Flour", 4 },
            { "Threadbare Loaf", "What Happened To Our First CD?", 17 },
            { "Therapy Zeppelin", "Mfbrbl Is Not A Word", 0 },
            { "Therapy Zeppelin", "Medium", 4 },
            { "Clancy in Silt", "Igneous", 5 }
    };

    private void buildModel() {
        for (Object[] data : model) {
            String artistName = (String) data[0];
            String songTitle = (String) data[1];
            Integer votes = (Integer) data[2];
            Optional<Artist> artistQuery = artistRepository
                    .findByNameIgnoreCase(artistName);
            Artist artist = artistQuery.orElseGet(() -> {
                Artist entity = createArtist(artistName);
                artistRepository.save(entity);
                return entity;
            });
            Optional<Song> songQuery = songRepository
                    .findByArtistIdAndNameIgnoreCase(artist.getId(),
                            songTitle);
            if (songQuery.isEmpty()) {
                Song song = createSong(artist, songTitle);
                song.setVotes(votes);
                songRepository.save(song);
            }
        }
    }

    @Test
    public void testOperations() {
        Artist artist = artistRepository
                .findByNameIgnoreCase("therapy zeppelin")
                .orElseThrow();
        List<Song> songs = songRepository
                .findByArtistIdAndNameLikeIgnoreCaseOrderByNameDesc(
                        artist.getId(),
                        converter.convertToWildCard("m"));
        assertEquals(songs.size(), 2);

        songs = songRepository
                .findByArtistIdOrderByVotesDesc(artist.getId());
        assertEquals(songs.size(), 2);

        // we know the votes assigned by default,
        // and they should be in descending order.

        // "Medium" has four votes...
        assertEquals(songs.get(0).getName(), "Medium");
        assertEquals(songs.get(0).getVotes(), 4);
        // "Mfbrbl" is liked by nobody. I mean, REALLY.
        assertEquals(songs.get(1).getVotes(), 0);
    }
}