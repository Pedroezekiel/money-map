package com.projects.money_map_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * MONEY MAP EXCEPTION
 *
 * Single flexible exception class for the entire application
 * Pass HTTP status code as parameter
 * Default status code: 400 BAD REQUEST
 *
 * Usage:
 *   throw new MoneyMapException("User not found", HttpStatus.NOT_FOUND);
 *   throw new MoneyMapException("Token expired", HttpStatus.UNAUTHORIZED);
 *   throw new MoneyMapException("Invalid input");  // defaults to 400
 */
@Getter
public class MoneyMapException extends RuntimeException {

    /**
     * -- GETTER --
     *  GET STATUS CODE
     *
     * @return The HTTP status code
     */
    private final HttpStatus statusCode;

    /**
     * Constructor with message and status code
     *
     * @param message The error message
     * @param statusCode The HTTP status code
     */
    public MoneyMapException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Constructor with message only
     * Defaults to 400 BAD REQUEST
     *
     * @param message The error message
     */
    public MoneyMapException(String message) {
        super(message);
        this.statusCode = HttpStatus.BAD_REQUEST;  // Default status code
    }

    /**
     * Constructor with message, status code, and cause
     *
     * @param message The error message
     * @param statusCode The HTTP status code
     * @param cause The root cause
     */
    public MoneyMapException(String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
