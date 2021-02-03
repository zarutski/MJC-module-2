package com.epam.esm.web.exception;

import com.epam.esm.dao.exception.EntityAlreadyExistsException;
import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.service.exception.IdNotExistException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int CODE_WRONG_DATA = 40001;
    private static final int CODE_BAD_REQUEST = 40002;
    private static final int CODE_NOT_FOUND = 40401;
    private static final int CODE_WRONG_ID = 40402;
    private static final int CODE_NOT_ALLOWED = 40501;
    private static final int CODE_ENTITY_EXISTS = 40901;
    private static final int CODE_DATA_FORMAT = 42201;
    private static final String KEY_WRONG_DATA = "incorrect_data";
    private static final String KEY_BAD_REQUEST = "bad_request_format";
    private static final String KEY_NOT_FOUND = "not_found";
    private static final String KEY_WRONG_ID = "wrong_resource_id";
    private static final String KEY_NOT_ALLOWED = "method_not_allowed";
    private static final String KEY_ENTITY_EXISTS = "conflict";
    private static final String KEY_DATA_FORMAT = "wrong_data_format";
    private static final String VALUES_DELIMITER = " = ";
    private static final String MESSAGE_EMPTY = "";

    private final ReloadableResourceBundleMessageSource resourceBundle;

    public APIExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIdNotExistException(IdNotExistException exception) {
        return getErrorResponse(KEY_WRONG_ID, CODE_WRONG_ID, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongInsertDataException(WrongInsertDataException exception) {
        return getErrorResponse(KEY_WRONG_DATA, CODE_WRONG_DATA, MESSAGE_EMPTY);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        return getErrorResponse(KEY_ENTITY_EXISTS, CODE_ENTITY_EXISTS, exception.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        // get field validation message
        String fieldMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + VALUES_DELIMITER + fieldError.getDefaultMessage())
                .collect(Collectors.toList())
                .toString();
        ErrorResponse response = getErrorResponse(KEY_DATA_FORMAT, CODE_DATA_FORMAT, fieldMessage);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(KEY_BAD_REQUEST, CODE_BAD_REQUEST, MESSAGE_EMPTY), status);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(KEY_NOT_ALLOWED, CODE_NOT_ALLOWED, MESSAGE_EMPTY), status);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(KEY_NOT_FOUND, CODE_NOT_FOUND, MESSAGE_EMPTY), status);
    }

    private ErrorResponse getErrorResponse(String key, int code, String exceptionMessage) {
        String bundleMessage = resourceBundle.getMessage(key, null, LocaleContextHolder.getLocale());
        return new ErrorResponse(bundleMessage + exceptionMessage, code);
    }
}