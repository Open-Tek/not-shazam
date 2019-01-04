package com.knowwhere.notshazamserver.utils;

public class UnauthorizedAccessException extends RuntimeException {
    private final static String MESSAGE = "You are not allowed to accsess resource %s.";

    /**
     * @param resource- -> The resource
     */
    public UnauthorizedAccessException(String resource){
        super(String.format(MESSAGE, resource));
    }

}
