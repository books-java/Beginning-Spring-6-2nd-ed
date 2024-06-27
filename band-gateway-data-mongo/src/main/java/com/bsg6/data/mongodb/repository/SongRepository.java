package com.bsg6.data.mongodb.repository;

import com.bsg6.data.mongodb.model.Artist;
import com.bsg6.data.mongodb.model.Song;
import com.bsg6.data.repository.BaseSongRepository;

public interface SongRepository
                extends BaseSongRepository<Artist, Song, String> {
}