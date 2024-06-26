package com.bsg6.data.model;

public interface BaseSong<T extends BaseArtist<ID>, ID> extends BaseEntity<ID> {
    T getArtist();

    void setArtist(T artist);

    int getVotes();

    void setVotes(int votes);
}