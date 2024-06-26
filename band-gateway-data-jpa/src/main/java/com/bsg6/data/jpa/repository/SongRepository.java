package com.bsg6.data.jpa.repository;

import com.bsg6.data.jpa.model.Artist;
import com.bsg6.data.jpa.model.Song;
import com.bsg6.data.repository.BaseSongRepository;

public interface SongRepository
                extends BaseSongRepository<Artist, Song, Integer> {
}