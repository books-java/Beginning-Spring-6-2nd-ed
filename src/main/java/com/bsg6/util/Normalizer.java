package com.bsg6.util;

public interface Normalizer {
    default String transform(String input) {
        return input.trim();
    }
}