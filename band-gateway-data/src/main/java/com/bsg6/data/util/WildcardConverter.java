package com.bsg6.data.util;

public class WildcardConverter {
    private final String append;

    public WildcardConverter(String append) {
        this.append = append;
    }

    public String convertToWildCard(String data) {
        return data + append;
    }
}