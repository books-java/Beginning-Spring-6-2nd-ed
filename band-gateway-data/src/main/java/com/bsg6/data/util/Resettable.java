package com.bsg6.data.util;
/*
 * We want to have another interface for testing purposes – a Resettable. This allows
 * us to mark a component as being able to be reset – something useful for us during testing.
 */
public interface Resettable {
    void reset();
}