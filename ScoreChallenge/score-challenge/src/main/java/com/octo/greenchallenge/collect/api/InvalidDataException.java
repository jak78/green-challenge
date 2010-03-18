package com.octo.greenchallenge.collect.api;

/**
 * Thrown when data given to the collect API is invalid. e.g source does not exist, CPUCycles is not an integer, and so forth.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String s) {
        super(s);
    }

    public InvalidDataException(String msg, Throwable cause) {
        super(msg,cause);
    }
}
