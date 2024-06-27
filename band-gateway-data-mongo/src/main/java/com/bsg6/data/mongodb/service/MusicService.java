package com.bsg6.data.mongodb.service;

import com.bsg6.data.mongodb.model.Artist;
import com.bsg6.data.mongodb.model.Song;
import com.bsg6.data.mongodb.repository.ArtistRepository;
import com.bsg6.data.mongodb.repository.SongRepository;
import com.bsg6.data.service.BaseMusicService;
import com.bsg6.data.util.WildcardConverter;

public class MusicService extends BaseMusicService<Artist, Song, String> {
    public MusicService(
        ArtistRepository artistRepository,
        SongRepository songRepository,
        WildcardConverter converter
    ) {
        super(artistRepository, songRepository, converter);
    }

    @Override
    protected Artist createArtist(String name) {
        return new Artist(name);
    }

    @Override
    protected Song createSong(Artist artist, String name) {
        return new Song(artist, name);
    }
}