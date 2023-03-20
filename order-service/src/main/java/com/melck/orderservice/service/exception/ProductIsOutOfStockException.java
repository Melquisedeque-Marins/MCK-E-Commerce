package com.melck.orderservice.service.exception;

public class ProductIsOutOfStockException extends RuntimeException {
    public ProductIsOutOfStockException(String message) {
        super(message);
    }

    public ProductIsOutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
