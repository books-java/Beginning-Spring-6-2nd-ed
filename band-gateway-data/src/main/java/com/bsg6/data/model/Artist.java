package com.bsg6.data.model;

import java.util.*;

public class Artist implements Comparable<Artist> {
    private String name;
    private int id;

    public Artist() {
    }

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Artist))
            return false;
        Artist artist = (Artist) o;
        return getId() == artist.getId() &&
                Objects.equals(
                        getName().toLowerCase(),
                        artist.getName().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public int compareTo(Artist o) {
        return o.getName().toLowerCase().compareTo(getName().toLowerCase());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ",
                Artist.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}