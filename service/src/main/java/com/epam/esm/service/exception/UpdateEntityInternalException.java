package com.epam.esm.service.exception;

public class UpdateEntityInternalException extends RuntimeException {

    public UpdateEntityInternalException() {
    }

    public UpdateEntityInternalException(String message) {
        super(message);
    }

    public UpdateEntityInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
