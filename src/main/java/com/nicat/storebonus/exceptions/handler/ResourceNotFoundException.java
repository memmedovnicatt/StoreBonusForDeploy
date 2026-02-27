package com.nicat.storebonus.exceptions.handler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object field) {
        super(String.format("%s not found with: %s", resourceName, field));
    }
}
