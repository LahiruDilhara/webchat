package me.lahirudilhara.webchat.configuration;

import jakarta.servlet.http.HttpServletRequest;
import me.lahirudilhara.webchat.core.exceptions.BaseException;
import me.lahirudilhara.webchat.core.lib.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    // Handle custom business exceptions
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BaseException ex, HttpServletRequest request) {

        log.warn("Business exception: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(error, ex.getCode());
    }

    // Handle validation errors - return only first error message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation error on path: {}", request.getRequestURI());

        // Get only the first validation error message
        String errorMessage = "Invalid request data";
        if (ex.getBindingResult().hasFieldErrors()) {
            org.springframework.validation.FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
            errorMessage = fieldError.getDefaultMessage();
        }

        ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle request parameter validation errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        log.warn("Constraint violation on path: {}", request.getRequestURI());

        // Get first constraint violation message
        String errorMessage = ex.getErrorMessage();


        ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle HTTP method not allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.warn("Method not allowed: {} on path: {}", ex.getMethod(), request.getRequestURI());

        ErrorResponse error = new ErrorResponse(
                "HTTP method not allowed for this endpoint",
                HttpStatus.METHOD_NOT_ALLOWED
        );
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle missing request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Message not readable on path: {}", request.getRequestURI());

        ErrorResponse error = new ErrorResponse(
                "Invalid or missing request body",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle missing path variables or request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("Missing parameter: {} on path: {}", ex.getParameterName(), request.getRequestURI());

        ErrorResponse error = new ErrorResponse(
                "Required parameter is missing: " + ex.getParameterName(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle database/system errors WITHOUT exposing details
    @ExceptionHandler({
            DataAccessException.class,
            SQLException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleDatabaseError(
            Exception ex, HttpServletRequest request) {

        // Log full error details for debugging (not exposed to client)
        log.error("Database error on path: {}", request.getRequestURI(), ex);

        // Return generic message to client
        ErrorResponse error = new ErrorResponse(
                "A system error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle authentication errors
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access denied on path: {}", request.getRequestURI());

        ErrorResponse error = new ErrorResponse(
                "You don't have permission to access this resource",
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Illegal argument on path: {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle resource not found (404 errors)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NoHandlerFoundException ex, HttpServletRequest request) {

        log.warn("Endpoint not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse error = new ErrorResponse(
                "The requested endpoint was not found",
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Bad credentials on path: {}", request.getRequestURI());

        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // Handle all other unexpected system errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(
            Exception ex, HttpServletRequest request) {

        // Log full error details for debugging (not exposed to client)
        log.error("Unexpected error on path: {}", request.getRequestURI(), ex);

        // Return generic message to client
        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred. Please contact support if the problem persists.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}