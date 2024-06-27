package com.bsg6.data.mongodb.repository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bsg6.data.mongodb.model.Artist;
import com.bsg6.data.util.WildcardConverter;

@DataMongoTest
public class ArtistRepositoryTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ArtistRepository artistRepository;

    // to allow access to createWildcard...
    @Autowired
    WildcardConverter converter;

    protected Artist createArtist(String name) {
        return new Artist(name);
    }

    @BeforeMethod
    public void clearDatabase() {
        artistRepository.deleteAll();
    }

    @Test
    public void testOperations() {
        // see if the database is empty
        assertEquals(artistRepository.count(), 0);

        var firstEntity = createArtist("Threadbare Loaf");
        var secondEntity = createArtist("Therapy Zeppelin");

        firstEntity = artistRepository.save(firstEntity);

        assertNotNull(firstEntity.getId());
        var artist = artistRepository.findById(firstEntity.getId());
        assertTrue(artist.isPresent());
        assertEquals(artist.get(), firstEntity);

        var query = artistRepository.findAllByNameIsLikeIgnoreCaseOrderByName(converter.convertToWildCard("th"));
        assertEquals(query.size(), 1l);
        assertEquals(query.get(0), firstEntity);

        artistRepository.save(secondEntity);
        query = artistRepository.findAllByNameIsLikeIgnoreCaseOrderByName(converter.convertToWildCard("th"));
        assertEquals(query.size(), 2);
    }

}