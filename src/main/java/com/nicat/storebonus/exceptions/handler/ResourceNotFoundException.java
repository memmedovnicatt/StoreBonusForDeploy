package com.nicat.storebonus.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object field) {
        super(String.format("%s not found with: %s", resourceName, field));
    }
}
