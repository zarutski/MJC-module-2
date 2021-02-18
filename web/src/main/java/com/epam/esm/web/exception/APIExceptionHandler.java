package com.epam.esm.web.exception;

import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.service.exception.UpdateEntityInternalException;
import com.epam.esm.service.exception.IdNotExistException;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int CODE_WRONG_DB_DATA = 40001;
    private static final int CODE_BAD_REQUEST = 40002;
    private static final int CODE_NOT_FOUND = 40401;
    private static final int CODE_WRONG_ID = 40402;
    private static final int CODE_NOT_ALLOWED = 40501;
    private static final int CODE_CONSTRAINT_VIOLATION = 40901;
    private static final int CODE_MEDIA_NOT_SUPPORTED = 41501;
    private static final int CODE_DATA_FORMAT = 42201;
    private static final int CODE_TYPE_MISMATCH = 42202;
    private static final int CODE_CREATE_INTERNAL = 50001;
    private static final int CODE_UPDATE_INTERNAL = 50002;

    private static final String KEY_WRONG_DB_DATA = "incorrect_data";
    private static final String KEY_BAD_REQUEST = "bad_request_format";
    private static final String KEY_NOT_FOUND = "not_found";
    private static final String KEY_WRONG_ID = "wrong_resource_id";
    private static final String KEY_NOT_ALLOWED = "method_not_allowed";
    private static final String KEY_CONSTRAINT_VIOLATION = "conflict";
    private static final String KEY_MEDIA_NOT_SUPPORTED = "media_not_supported";
    private static final String KEY_DATA_FORMAT = "wrong_data_format";
    private static final String KEY_TYPE_MISMATCH = "type_mismatch";
    private static final String CREATE_INTERNAL_ERROR = "create_internal";
    private static final String UPDATE_INTERNAL_ERROR = "update_internal";

    private static final String VALUES_DELIMITER = " = ";
    private static final String MESSAGE_EMPTY = "";

    private final ReloadableResourceBundleMessageSource resourceBundle;

    public APIExceptionHandler(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMysqlDataTruncation(MysqlDataTruncation exception) {
        return getErrorResponse(KEY_WRONG_DB_DATA, CODE_WRONG_DB_DATA, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIdNotExistException(IdNotExistException exception) {
        return getErrorResponse(KEY_WRONG_ID, CODE_WRONG_ID, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return getErrorResponse(KEY_CONSTRAINT_VIOLATION, CODE_CONSTRAINT_VIOLATION,
                exception.getErrorCode() + MESSAGE_EMPTY);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleViolationException(javax.validation.ConstraintViolationException exception) {
        return getErrorResponse(KEY_DATA_FORMAT, CODE_DATA_FORMAT,
                exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleCreateEntityInternalException(CreateEntityInternalException exception) {
        return getErrorResponse(CREATE_INTERNAL_ERROR, CODE_CREATE_INTERNAL, MESSAGE_EMPTY);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUpdateEntityInternalException(UpdateEntityInternalException exception) {
        return getErrorResponse(UPDATE_INTERNAL_ERROR, CODE_UPDATE_INTERNAL, MESSAGE_EMPTY);
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
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = getErrorResponse(KEY_TYPE_MISMATCH, CODE_TYPE_MISMATCH, MESSAGE_EMPTY);
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
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        // get content type message
        String message = MESSAGE_EMPTY;
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            message = contentType.toString();
        }
        return new ResponseEntity<>(getErrorResponse(KEY_MEDIA_NOT_SUPPORTED, CODE_MEDIA_NOT_SUPPORTED, message), status);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(KEY_NOT_FOUND, CODE_NOT_FOUND, MESSAGE_EMPTY), status);
    }

    private ErrorResponse getErrorResponse(String key, int code, String exceptionMessage) {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        Locale locale = request.getLocale();
        return new ErrorResponse(resourceBundle.getMessage(key, null, locale) + exceptionMessage, code);
    }
}