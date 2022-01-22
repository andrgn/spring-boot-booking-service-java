package com.booker.demo.aspects;

import com.booker.demo.utils.ApiError;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Objects;

@ControllerAdvice
public class BookingAspect extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var errorMessages = new ArrayList<String>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    var errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    errorMessages.add(errorMessage);
                });
        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(globalError -> {
                    var errorMessage = globalError.getObjectName() + ": " + globalError.getDefaultMessage();
                    errorMessages.add(errorMessage);
                });
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessages);

        return handleExceptionInternal(ex, apiError, headers, apiError.status(), request);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var errorMessage = ex.getParameterName() + " parameter is missing";
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);
        var httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(apiError, httpHeaders, apiError.status());
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var errorMessage = ex.getMethod() + " method isn't supported for this request";
        var apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, errorMessage);
        var httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(apiError, httpHeaders, apiError.status());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        var errorMessage = ex.getName() + " must be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);
        var httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(apiError, httpHeaders, apiError.status());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleEmptyResultDataAccessException(
            EmptyResultDataAccessException ex
    ) {
        var errorMessage = "There is no such id";
        var apiError = new ApiError(HttpStatus.NOT_FOUND, errorMessage);
        var httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(apiError, httpHeaders, apiError.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        var errorMessage = "Error occurred";
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        var httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(apiError, httpHeaders, apiError.status());
    }
}
