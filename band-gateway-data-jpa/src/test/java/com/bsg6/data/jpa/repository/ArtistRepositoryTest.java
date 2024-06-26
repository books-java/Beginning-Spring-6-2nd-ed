package com.bsg6.data.jpa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bsg6.data.jpa.model.Artist;
import com.bsg6.data.util.WildcardConverter;

import static org.testng.Assert.*;

@DataJpaTest
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