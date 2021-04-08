package com.cdc.commons.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class EntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ObjectError> errors = this.getErrors(ex);
        ErrorResponse errorResponse = this.getErrorResponse(
                "The request contains invalid fields", HttpStatus.BAD_REQUEST,
                request, errors);
        this.logException(ex);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        this.logException(ex);
        return ResponseEntity.badRequest().body(this.getErrorResponse(
                ex.getMessage(), HttpStatus.CONFLICT,
                request, null));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        this.logException(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.getErrorResponse(
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
                request, null));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        this.logException(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                this.getErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request, null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        this.logException(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                this.getErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, null));
    }

    private ErrorResponse getErrorResponse(String message, HttpStatus status, HttpServletRequest request, List<ObjectError> errors) {
        return ErrorResponse.builder()
                .message(message)
                .status(status.getReasonPhrase())
                .code(status.value())
                .errors(errors)
                .timestamp(Instant.now().toString())
                .path(request.getRequestURI())
                .build();
    }

    private List<ObjectError> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ObjectError(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }

    private void logException(Exception ex) {
        log.error("Error", ex);
    }

}
