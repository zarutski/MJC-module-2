package com.epam.esm.web.exception;

import com.epam.esm.dao.exception.EntityAlreadyExistsException;
import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.service.exception.IdNotExistException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int ERROR_CODE_WRONG_ID = 40401;
    private static final int ERROR_CODE_WRONG_DATA = 40001;
    private static final int ERROR_CODE_ENTITY_EXISTS = 40901;
    private static final int ERROR_CODE_BAD_REQUEST = 40002;
    private static final String MESSAGE_KEY_WRONG_ID = "wrong_resource_id";
    private static final String MESSAGE_KEY_WRONG_DATA = "incorrect_data";
    private static final String MESSAGE_KEY_ENTITY_EXISTS = "conflict";
    private static final String MESSAGE_KEY_BAD_REQUEST = "bad_request_format";
    public static final String EXCEPTION_MESSAGE_EMPTY = "";

    private final ReloadableResourceBundleMessageSource resourceBundle;

    public APIExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIdNotExistException(IdNotExistException exception) {
        return getErrorResponse(MESSAGE_KEY_WRONG_ID, ERROR_CODE_WRONG_ID, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongInsertDataException(WrongInsertDataException exception) {
        return getErrorResponse(MESSAGE_KEY_WRONG_DATA, ERROR_CODE_WRONG_DATA, EXCEPTION_MESSAGE_EMPTY);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleWrongInsertDataException(EntityAlreadyExistsException exception) {
        return getErrorResponse(MESSAGE_KEY_ENTITY_EXISTS, ERROR_CODE_ENTITY_EXISTS, exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(MESSAGE_KEY_BAD_REQUEST, ERROR_CODE_BAD_REQUEST, EXCEPTION_MESSAGE_EMPTY), status);
    }

    private ErrorResponse getErrorResponse(String key, int code, String exceptionMessage) {
        String bundleMessage = resourceBundle.getMessage(key, null, LocaleContextHolder.getLocale());
        return new ErrorResponse(bundleMessage + exceptionMessage, code);
    }
}