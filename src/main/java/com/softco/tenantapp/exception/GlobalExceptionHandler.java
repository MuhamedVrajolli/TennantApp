package com.softco.tenantapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for managing application exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles TenantNotFoundException.
   *
   * @param ex the exception
   * @param request the current web request
   * @return a ResponseEntity with error details
   */
  @ExceptionHandler(TenantNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTenantNotFoundException(TenantNotFoundException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, ex);
  }

  /**
   * Handles RequestProcessingException.
   *
   * @param ex the exception
   * @param request the current web request
   * @return a ResponseEntity with error details
   */
  @ExceptionHandler(RequestProcessingException.class)
  public ResponseEntity<ErrorResponse> handleRequestProcessingException(RequestProcessingException ex,
      WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  /**
   * Handles MissingRequestHeaderException.
   *
   * @param ex the exception
   * @param request the current web request
   * @return a ResponseEntity with error details
   */
  @ExceptionHandler(MissingHeaderException.class)
  public ResponseEntity<ErrorResponse> handleMissingHeaderException(MissingHeaderException ex,
      WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, ex);
  }

  /**
   * Handles all other exceptions.
   *
   * @param ex the exception
   * @param request the current web request
   * @return a ResponseEntity with error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
    return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  /**
   * Constructs a standardized error response.
   *
   * @param message the error message
   * @param status the HTTP status
   * @param ex the exception
   * @return a ResponseEntity with an ErrorResponse body
   */
  private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(message, ex.getClass().getSimpleName());
    return ResponseEntity.status(status).body(errorResponse);
  }
}
