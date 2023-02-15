package com.melck.userservice.service.exception;

public class CpfAlreadyInUseException extends RuntimeException {
    public CpfAlreadyInUseException(String message) {
        super(message);
    }

    public CpfAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
