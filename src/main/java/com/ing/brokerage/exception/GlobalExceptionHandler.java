package com.ing.brokerage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayberk.altuntabak
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle IllegalArgumentExceptions (e.g., for insufficient funds or invalid data)
   *
   * @param ex - The exception
   * @return ResponseEntity with error message and BAD_REQUEST status
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle validation errors (e.g., @Valid failures)
   *
   * @param ex - The MethodArgumentNotValidException
   * @return ResponseEntity with validation error messages and BAD_REQUEST status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Generic handler for other types of exceptions (fallback)
   *
   * @param ex - The exception
   * @return ResponseEntity with error message and INTERNAL_SERVER_ERROR status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "An unexpected error occurred.");
    response.put("details", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
