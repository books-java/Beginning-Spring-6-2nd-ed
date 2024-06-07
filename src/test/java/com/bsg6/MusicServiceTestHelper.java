package com.bsg6;

import java.util.List;
import java.util.function.Consumer;

import com.bsg6.model.Song;
import com.bsg6.service.MusicService;
import com.bsg6.util.Resettable;
import static org.testng.Assert.assertEquals;

public class MusicServiceTestHelper {
     private Object[][] model = new Object[][]{
            {"Threadbare Loaf", "Someone Stole the Flour", 4},
            {"Threadbare Loaf", "What Happened To Our First CD?", 17},
            {"Therapy Zeppelin", "Medium", 4},
            {"Clancy in Silt", "Igneous", 5}
    };
    /*
     * allow us to execute a simple method over every row in the model; it accepts a
     * Consumer<Object[]>, which means a lambda that expects an Object[], and calls the
     * lambda for every row
     */
    void iterateOverModel(Consumer<Object[]> consumer) {
        for (Object[] data : model) {
            consumer.accept(data);
        }
    }
    /*
     * creates a lambda that issues a single vote for a song and calls that lambda multiple times for every row in our model
     */
    void populateService(MusicService service) {
        iterateOverModel(data -> {
            for (int i = 0; i < (Integer) data[2]; i++) {
                service.voteForSong((String) data[0], (String) data[1]);
            }
        });
    }

    void reset(MusicService service) {
        if (service instanceof Resettable) {
            ((Resettable) service).reset();
        } else {
            throw new RuntimeException(service +
                    " does not implement Resettable.");
        }
    }

    void testSongVoting(MusicService service) {
        reset(service);
        populateService(service);
        iterateOverModel(data ->
                assertEquals(
                        service.getSong((String) data[0],
                                (String) data[1]).getVotes(),
                        ((Integer) data[2]).intValue()
                ));
    }

    void testSongsForArtist(MusicService service) {
        reset(service);
        populateService(service);
        List<Song> songs = service.getSongsForArtist("Threadbare Loaf");
        assertEquals(songs.size(), 2);
        assertEquals(songs.get(0).getName(), "What Happened To Our First CD?");
        assertEquals(songs.get(0).getVotes(), 17);
        assertEquals(songs.get(1).getName(), "Someone Stole the Flour");
        assertEquals(songs.get(1).getVotes(), 4);
    }

    void testMatchingArtistNames(MusicService service) {
        reset(service);
        populateService(service);
        List<String> names = service.getMatchingArtistNames("Th");
        assertEquals(names.size(), 2);
        assertEquals(names.get(0), "Therapy Zeppelin");
        assertEquals(names.get(1), "Threadbare Loaf");
    }

    void testMatchingSongNamesForArtist(MusicService service) {
        reset(service);
        populateService(service);
        List<String> names = service.getMatchingSongNamesForArtist(
                "Threadbare Loaf", "W"
        );
        assertEquals(names.size(), 1);
        assertEquals(names.get(0), "What Happened To Our First CD?");
    }
}
