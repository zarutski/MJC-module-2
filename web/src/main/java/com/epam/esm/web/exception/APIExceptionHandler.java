package com.epam.esm.web.exception;

import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.service.exception.IdNotExistException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int ERROR_CODE_WRONG_ID = 40401;
    private static final int ERROR_CODE_WRONG_DATA = 40001;
    private static final int ERROR_CODE_BAD_REQUEST = 40002;
    private static final String MESSAGE_KEY_WRONG_ID = "wrong_resource_id";
    private static final String MESSAGE_KEY_WRONG_DATA = "incorrect_data";
    private static final String MESSAGE_KEY_BAD_REQUEST = "bad_request_format";

    private final ReloadableResourceBundleMessageSource resourceBundle;

    public APIExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleIdNotExistException(IdNotExistException exception) {
        String bundleMessage = resourceBundle.getMessage(MESSAGE_KEY_WRONG_ID, null, LocaleContextHolder.getLocale());
        String errorMessage = bundleMessage + exception.getMessage();
        return new ResponseEntity<>(new ErrorHandler(errorMessage, ERROR_CODE_WRONG_ID), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleWrongInsertDataException(WrongInsertDataException exception) {
        String bundleMessage = resourceBundle.getMessage(MESSAGE_KEY_WRONG_DATA, null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ErrorHandler(bundleMessage, ERROR_CODE_WRONG_DATA), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleException(Exception e) {
        String bundleMessage = resourceBundle.getMessage(MESSAGE_KEY_BAD_REQUEST, null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ErrorHandler(bundleMessage, ERROR_CODE_BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

}