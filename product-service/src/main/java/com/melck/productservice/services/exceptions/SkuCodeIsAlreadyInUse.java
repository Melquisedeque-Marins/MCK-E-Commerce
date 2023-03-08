package com.melck.productservice.services.exceptions;

public class SkuCodeIsAlreadyInUse extends RuntimeException {
    public SkuCodeIsAlreadyInUse(String message) {
        super(message);
    }

    public SkuCodeIsAlreadyInUse(String message, Throwable cause) {
        super(message, cause);
    }
}
