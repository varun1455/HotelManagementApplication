package com.project.stayEase.customExceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
            super(message);
    }
}
