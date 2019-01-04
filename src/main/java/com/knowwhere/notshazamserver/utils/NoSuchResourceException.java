package com.knowwhere.notshazamserver.utils;

public class NoSuchResourceException extends RuntimeException{
    public static String MESSAGE = "Resource %s with property %s with value %s doesnt exist.";

    /**
     * @param resource ->The resource that led to the exception
     * @param property -> The property that this resource has
     * @param value -> The value that this property had that led to this exception
     */
    public NoSuchResourceException(String resource, String property, String value ){
        super(String.format(MESSAGE, resource, property, value));
    }
}
