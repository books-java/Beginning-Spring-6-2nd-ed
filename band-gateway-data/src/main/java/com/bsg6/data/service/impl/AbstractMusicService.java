package com.bsg6.data.service.impl;

import com.bsg6.data.model.Artist;
import com.bsg6.data.model.Song;
import com.bsg6.data.service.MusicService;
import com.bsg6.data.util.Resettable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractMusicService implements MusicService, Resettable {
    private Map<String, Artist> bands = new HashMap<>();

    protected String transformArtist(String input) {
        return input;
    }

    protected String transformSong(String input) {
        return input;
    }

    @Override
    public void reset() {
        bands.clear();
    }

    private Artist getArtist(String name) {
        String normalizedName = transformArtist(name);
        return bands.computeIfAbsent(normalizedName,
                s -> new Artist(normalizedName));
    }

    @Override
    public Song getSong(String artistName, String name) {
        Artist artist = getArtist(artistName);
        String normalizedTitle = transformSong(name);
        return artist
                .getSongs()
                .computeIfAbsent(normalizedTitle, Song::new);
    }

    @Override
    public List<Song> getSongsForArtist(String artist) {
        List<Song> songs = new ArrayList<>(
                getArtist(artist)
                        .getSongs()
                        .values());
        songs.sort(Song::compareTo);
        return songs;
    }

    @Override
    public List<String> getMatchingSongNamesForArtist(String artist,
            String prefix) {
        String normalizedPrefix = transformSong(prefix)
                .toLowerCase();

        /**
         * This is a fun operation!
         *
         * We get the artist's songs, which are in a map.
         * We then get only the keys of the map, which are all we care about.
         * Next, we want to process those keys in a stream...
         * We filter the names by looking for titles that start with
         * our prefix..
         * Then sort them in alphabetical order
         * And return them in a list.
         */
        return getArtist(artist)
                .getSongs()
                .keySet()
                .stream()
                .map(this::transformSong)
                .filter(name -> name
                        .toLowerCase()
                        .startsWith(normalizedPrefix))
                .sorted(Comparator.comparing(Function.identity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMatchingArtistNames(String prefix) {
        String normalizedPrefix = transformArtist(prefix)
                .toLowerCase();
        return bands
                .keySet()
                .stream()
                .filter(name -> name
                        .toLowerCase()
                        .startsWith(normalizedPrefix))
                .sorted(Comparator.comparing(Function.identity()))
                .collect(Collectors.toList());
    }

    @Override
    public Song voteForSong(String artistName, String name) {
        Song song = getSong(artistName, name);
        song.setVotes(song.getVotes() + 1);
        return song;
    }
}
