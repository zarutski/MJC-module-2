package com.epam.esm.dao.exception;

public class EntityNotInDBException extends RuntimeException {

    public EntityNotInDBException() {
    }

    public EntityNotInDBException(String message) {
        super(message);
    }

    public EntityNotInDBException(String message, Throwable cause) {
        super(message, cause);
    }

}