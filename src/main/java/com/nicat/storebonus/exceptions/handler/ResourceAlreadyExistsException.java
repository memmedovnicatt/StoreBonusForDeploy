package com.nicat.storebonus.exceptions.handler;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(Object field) {
        super(String.format("%s was already exists:", field));
    }
}
