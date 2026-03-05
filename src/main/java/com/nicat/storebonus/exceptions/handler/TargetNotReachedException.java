package com.nicat.storebonus.exceptions.handler;

public class TargetNotReachedException extends RuntimeException {
    public TargetNotReachedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s was not reached to %s.Target was:%s", resourceName, fieldName, fieldValue));
    }
}