package com.cdc.commons.exceptions;

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
public class EntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ObjectError> errors = this.getErrors(ex);
        ErrorResponse errorResponse = this.getErrorResponse(
                "The request contains invalid fields", HttpStatus.BAD_REQUEST,
                request, errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = this.getErrorResponse(
                ex.getMessage(), HttpStatus.CONFLICT,
                request, null);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = this.getErrorResponse(
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
                request, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
}
