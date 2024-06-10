package com.bsg6.data.service;

import java.util.List;

import com.bsg6.data.model.Song;
/*
 * It really would be better,
 * in a real-world application, to split the API calls related to Artist into an ArtistService,
 * and the other API calls into a SongService, and then have the MusicService delegate to
 * concrete implementations of those interfaces
 */
public interface MusicService {

    List<Song> getSongsForArtist(String artist);

    List<String> getMatchingSongNamesForArtist(String artist, String prefix);

    List<String> getMatchingArtistNames(String prefix);

    Song getSong(String artist, String name);

    Song voteForSong(String artist, String name);
}