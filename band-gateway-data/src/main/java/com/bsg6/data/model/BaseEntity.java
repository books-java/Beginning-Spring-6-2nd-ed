package com.bsg6.data.model;

public interface BaseEntity<ID> {
    ID getId();

    void setId(ID id);

    /**
     * Get the entity name
     */
    String getName();

    void setName(String name);
}