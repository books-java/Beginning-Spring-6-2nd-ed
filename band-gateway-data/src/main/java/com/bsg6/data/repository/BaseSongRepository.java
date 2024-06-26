package com.bsg6.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bsg6.data.model.BaseArtist;
import com.bsg6.data.model.BaseSong;

public interface BaseSongRepository<A extends BaseArtist<ID>, S extends BaseSong<A, ID>, ID>
        extends CrudRepository<S, ID> {

    List<S> findAllByNameIsLikeIgnoreCaseOrderByName(String name);

    Optional<S> findByNameIgnoreCase(String name);

    Optional<S> findByArtistIdAndNameIgnoreCase(
            ID artistId, String name);

    List<S> findByArtistIdOrderByVotesDesc(ID artistId);

    List<S> findByArtistIdAndNameLikeIgnoreCaseOrderByNameDesc(
            ID artistId, String name);
}