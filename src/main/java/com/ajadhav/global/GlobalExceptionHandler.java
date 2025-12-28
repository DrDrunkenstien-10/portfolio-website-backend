package com.ajadhav.global;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ajadhav.contact.exceptions.EmailSendException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<?> handleEmailException(EmailSendException e) {
        log.warn("Email sending failed: {}", e.getMessage());

        return ResponseEntity
                .status(500)
                .body(Map.of(
                        "error", e.getMessage(),
                        "timestamp", LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField, FieldError::getDefaultMessage));

        log.warn("Validation failed for request: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}
