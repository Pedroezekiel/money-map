package com.projects.money_map_api.exception;

import com.projects.money_map_api.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * GLOBAL EXCEPTION HANDLER
 *
 * Catches MoneyMapException from entire application
 * Returns standardized error response
 * Uses the status code from the exception
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * HANDLE MONEY MAP EXCEPTION
     *
     * Catches any MoneyMapException thrown in the application
     * Returns the status code that was passed to the exception
     */
    @ExceptionHandler(MoneyMapException.class)
    public ResponseEntity<BaseResponse<String>> handleMoneyMapException(
            MoneyMapException ex,
            WebRequest request) {

        BaseResponse<String> response = BaseResponse.<String>builder()
                .status(ex.getStatusCode().value())
                .data(ex.getStatusCode().getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        // Log the error
        log.error("MoneyMapException: {} - {}", ex.getStatusCode(), ex.getMessage());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    /**
     * HANDLE VALIDATION EXCEPTIONS
     *
     * When @Valid annotation fails on request body
     * Status: 400 BAD REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // Extract first validation error
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        BaseResponse<String> response = BaseResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        log.error("Validation error: {}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * HANDLE GENERIC EXCEPTIONS
     *
     * Catches any other unexpected exceptions
     * Status: 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGlobalException(
            Exception ex,
            WebRequest request) {

        BaseResponse<String> response = BaseResponse.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.error("Unexpected exception: ", ex);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}