package com.bsg6.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bsg6.data.model.BaseArtist;

public interface BaseArtistRepository<T extends BaseArtist<ID>, ID>
        extends CrudRepository<T, ID> {
            
    List<T> findAllByNameIsLikeIgnoreCaseOrderByName(String name);

    Optional<T> findByNameIgnoreCase(String name);
}