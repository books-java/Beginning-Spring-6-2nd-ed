package com.bsg6.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.util.UriUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bsg6.data.jpa.model.Artist;
import com.bsg6.data.jpa.repository.ArtistRepository;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

//@Sql(scripts = { "/schema.sql","/data.sql"}, executionPhase = BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistControllerTest extends AbstractTestNGSpringContextTests {
        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        ArtistRepository artistRepository;

        String encode(Object data) {
                return UriUtils.encode(data.toString(),
                                Charset.defaultCharset());
        }

        List<Object[]> getArtists() {
                /*
                 * return jdbcTemplate.query(
                 * "SELECT id, name FROM artists",
                 * (rs, rowNum) -> new Object[] {
                 * rs.getInt("id"),
                 * rs.getString("name")
                 * }
                 * 
                 * );
                 */
                return Streamable.of(artistRepository.findAll())
                                .stream()
                                .map(A -> new Object[] { A.getId(), A.getName() })
                                .collect(Collectors.toList());
        }

        @DataProvider
        Object[][] artistData() {
                List<Object[]> artists = getArtists();
                artists.add(new Object[] { -1, null });
                artists.add(new Object[] { -1, "Not A Band" });
                return artists.toArray(new Object[0][]);
        }

        // TODO fix this
        @Test(dataProvider = "artistData")
        public void testGetArtistById(int id, String name) {
                String url = "/artists/" + id;
                ResponseEntity<Artist> response = restTemplate.getForEntity(url, Artist.class);
                if (id != -1) {
                        assertEquals(response.getStatusCode(), HttpStatus.OK);
                        Artist artist = response.getBody();

                        assertEquals(artist.getId(), id);
                        // note: the corrected service returns the *proper* name
                        assertEquals(
                                        artist.getName().toLowerCase(),
                                        name.toLowerCase());
                } else {
                        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
                }
        }

        @Test(dataProvider = "artistData")
        public void testSearchForArtist(int id, String name) {
                String url = "/artists/search/" +
                                (name != null ? encode(name) : "");
                ResponseEntity<Artist> response = restTemplate.getForEntity(url, Artist.class);
                if (name != null) {
                        if (id == -1) {
                                assertEquals(response.getStatusCode(),
                                                HttpStatus.NOT_FOUND);
                        } else {
                                assertEquals(response.getStatusCode(), HttpStatus.OK);
                                Artist artist = response.getBody();

                                // note: the corrected service returns the *proper* name
                                assertEquals(
                                                artist.getName().toLowerCase(),
                                                name.toLowerCase());
                        }
                } else {
                        assertEquals(
                                        response.getStatusCode(),
                                        HttpStatus.BAD_REQUEST);
                }
        }

        @Test
        public void testSaveExistingArtist() {
                String url = "/artists";
                Artist newArtist = new Artist("Threadbare Loaf");

                ResponseEntity<Artist> response = restTemplate.postForEntity(
                                url,
                                newArtist,
                                Artist.class);

                assertEquals(response.getStatusCode(), HttpStatus.OK);
                Artist artist = response.getBody();
                assertNotNull(artist);

                int id = artist.getId();
                assertEquals(artist.getName(), newArtist.getName());

                response = restTemplate.postForEntity(
                                url,
                                newArtist,
                                Artist.class);
                assertEquals(response.getStatusCode(), HttpStatus.OK);
                assertEquals(response.getBody(), artist);
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
                ParameterizedTypeReference<List<String>> type = new ParameterizedTypeReference<>() {
                };
                String url = "/artists/match/" + encode(name);
                ResponseEntity<List<String>> response = restTemplate.exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                type);
                assertEquals(response.getStatusCode(), HttpStatus.OK);
                List<String> artists = response.getBody();
                assertNotNull(artists);
                assertEquals(artists.size(), count);
        }

        // We need this to run AFTER testSearches completes, because
        // testSearches adds to the artist list and therefore we
        // might get one more artist than we're expecting out of
        // some searches.
        @Test(dependsOnMethods = "testSearches")
        public void testSaveArtist() {
                String url = "/artists";
                Artist newArtist = new Artist("The Broken Keyboards");

                ResponseEntity<Artist> response = restTemplate.postForEntity(
                                url,
                                newArtist,
                                Artist.class);
                assertEquals(response.getStatusCode(), HttpStatus.OK);

                Artist artist = response.getBody();
                assertNotNull(artist);
                int id = artist.getId();
                assertNotEquals(id, 0);
                assertEquals(artist.getName(), newArtist.getName());

                response = restTemplate.getForEntity(url + "/search/" + newArtist.getName(), Artist.class);
                assertEquals(response.getStatusCode(), HttpStatus.OK);

                Artist foundArtist = response.getBody();
                assertNotNull(foundArtist);
                assertEquals(artist, foundArtist);
        }
}