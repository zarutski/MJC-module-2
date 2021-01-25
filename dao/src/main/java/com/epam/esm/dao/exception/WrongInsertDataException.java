package com.epam.esm.dao.exception;

public class WrongInsertDataException extends RuntimeException {

    public WrongInsertDataException() {
    }

    public WrongInsertDataException(String message) {
        super(message);
    }

    public WrongInsertDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
