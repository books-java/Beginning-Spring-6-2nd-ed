package com.bsg6.data.service;

import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

import com.bsg6.data.model.BaseArtist;
import com.bsg6.data.model.BaseSong;
import com.bsg6.data.repository.BaseArtistRepository;
import com.bsg6.data.repository.BaseSongRepository;
import com.bsg6.data.util.WildcardConverter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMusicService<A extends BaseArtist<ID>, S extends BaseSong<A, ID>, ID> {
    private final BaseArtistRepository<A, ID> artistRepository;
    private final BaseSongRepository<A, S, ID> songRepository;
    private final WildcardConverter converter;

    protected BaseMusicService(
            BaseArtistRepository<A, ID> artistRepository,
            BaseSongRepository<A, S, ID> songRepository,
            WildcardConverter converter) {
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.converter = converter;
    }

    protected abstract A createArtist(String name);

    protected abstract S createSong(A artist, String name);

    @Transactional
    public S voteForSong(String artistName, String songTitle) {
        S song = getSong(artistName, songTitle);
        song.setVotes(song.getVotes() + 1);
        return songRepository.save(song);
    }

    @Transactional
    public S getSong(String artistName, String songTitle) {
        A artist = getArtist(artistName);
        return songRepository
                .findByArtistIdAndNameIgnoreCase(
                        artist.getId(),
                        songTitle)
                .orElseGet(() -> {
                    S entity = createSong(artist, songTitle);
                    songRepository.save(entity);
                    return entity;
                });
    }

    @Transactional
    public List<A> getArtists() {
        return Streamable.of(artistRepository.findAll())
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional
    public A getArtist(String artistName) {
        return artistRepository
                .findByNameIgnoreCase(artistName)
                .orElseGet(() -> {
                    A entity = createArtist(artistName);
                    artistRepository.save(entity);
                    return entity;
                });
    }

    public A findArtistByName(String artistName) {
        return artistRepository
                .findByNameIgnoreCase(artistName).orElseGet(() -> null);
    }

    @Transactional
    public List<S> getSongsForArtist(String artistName) {
        A artist = getArtist(artistName);
        return songRepository.findByArtistIdOrderByVotesDesc(
                artist.getId());
    }

    @Transactional(readOnly = true)
    public List<String> getMatchingArtistNames(String artistName) {
        return artistRepository
                .findAllByNameIsLikeIgnoreCaseOrderByName(
                        converter.convertToWildCard(artistName))
                .stream()
                .map(A::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public A getArtistById(ID id) {
        return artistRepository.findById(id).orElse(null);
    }

    @Transactional
    public S getSongById(ID id) {
        return songRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<String> getMatchingSongNamesForArtist(
            String artistName,
            String songTitle) {
        A artist = getArtist(artistName);
        return songRepository
                .findByArtistIdAndNameLikeIgnoreCaseOrderByNameDesc(
                        artist.getId(),
                        converter.convertToWildCard(songTitle))
                .stream()
                .map(S::getName)
                .collect(Collectors.toList());
    }
}
