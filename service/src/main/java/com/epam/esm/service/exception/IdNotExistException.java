package com.epam.esm.service.exception;

public class IdNotExistException extends RuntimeException {

    public IdNotExistException() {
    }

    public IdNotExistException(String message) {
        super(message);
    }

    public IdNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}