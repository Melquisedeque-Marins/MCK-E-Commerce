package com.melck.inventoryservice.service.exceptions;

public class SkuCodeAlreadyRegisteredException extends RuntimeException {
    public SkuCodeAlreadyRegisteredException(String message) {
        super(message);
    }

    public SkuCodeAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
