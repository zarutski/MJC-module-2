package com.epam.esm.service.exception;

public class CreateEntityInternalException extends RuntimeException {

    public CreateEntityInternalException() {
    }

    public CreateEntityInternalException(String message) {
        super(message);
    }

    public CreateEntityInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}