package com.melck.userservice.service.exception;

public class AttributeAlreadyInUseException extends RuntimeException {
    public AttributeAlreadyInUseException(String message) {
        super(message);
    }

    public AttributeAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
