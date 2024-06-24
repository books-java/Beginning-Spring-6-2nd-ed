package com.bsg6.api;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import javax.swing.Spring;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.bsg6.data.MusicRepository;
import com.bsg6.data.model.Artist;
import com.bsg6.exceptions.ArtistNotFoundException;
import com.bsg6.exceptions.NoArtistNameSubmittedException;

@RestController
public class ArtistController {
    private MusicRepository service;

    ArtistController(MusicRepository service) {
        this.service = service;
    }

    String decode(Object data) {
        return UriUtils.decode(data.toString(), Charset.defaultCharset());
    }

    @GetMapping(value = "/artists/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Artist findArtistById(@PathVariable int id) {
        Artist artist = service.findArtistById(id);
        if (artist != null) {
            return artist;
        } else {
            throw new ArtistNotFoundException();
        }
    }

    /*
     * if no artist name is provided, the exception path is
     * always chosen and an IllegalArgumentException is thrown.
     */
    @GetMapping(value = { "/artists/search/{name}", "/artists/search/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    Artist findArtistByName(
            @PathVariable(required = false) String name) {
        if (name != null) {
            Artist artist = service.findArtistByNameNoUpdate(decode(name));
            if (artist != null) {
                return artist;
            } else {
                throw new ArtistNotFoundException();
            }
        } else {
            throw new NoArtistNameSubmittedException();
        }
    }

    /*
     * this method serves to migrate the MusicService' findArtistByName()
     * to something that accepts and returns an Optional
     */
    Optional<Artist> findArtistByName(Optional<String> name, boolean update) {
        return Optional.of(service.findArtistByName(
                decode(name.orElseThrow(NoArtistNameSubmittedException::new))));
    }

    // @GetMapping({ "/artist/search/{name}", "/artist/search/" })
    Artist findArtistByName(
            @PathVariable(required = false) Optional<String> name) {
        Optional<Artist> artistOptional = findArtistByName(name, false);
        return artistOptional.orElseThrow(ArtistNotFoundException::new);
    }

    @PostMapping(value = "/artists", produces = MediaType.APPLICATION_JSON_VALUE)
    Artist saveArtist(@RequestBody Artist artist) {
        return service.findArtistByName(artist.getName());
    }

    @GetMapping(value = { "/artists/match/{name}", "/artists/match/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> findArtistByMatchingName(
            @PathVariable(required = false) String name) {
        return service.getMatchingArtistNames(name != null ? decode(name) : "");
    }
}