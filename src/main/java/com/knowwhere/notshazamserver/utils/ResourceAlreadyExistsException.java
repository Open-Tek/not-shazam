package com.knowwhere.notshazamserver.utils;

public class ResourceAlreadyExistsException extends RuntimeException {
    private final static String MESSAGE = "Resource %s with property %s with value %s already exists";

    /**
     * @param resource ->The resource that led to the exception
     * @param property -> The property that this resource has
     * @param value -> The value that this property had that led to this exception
     */
    public ResourceAlreadyExistsException(String resource, String property, String value){
        super(String.format(MESSAGE, resource, property, value));
    }
}
