package me.catzy.invester.generic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Obsługa konkretnych wyjątków, np. EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFoundException(EntityNotFoundException ex) {
    	ex.printStackTrace();
        return Map.of("error", "Entity not found", "message", ex.getMessage());
    }

    // Obsługa ogólnych wyjątków
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
    	ex.printStackTrace();
        return Map.of("error", "Internal server error", "message", ex.getMessage());
    }

    // Obsługa błędów walidacji (np. @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	ex.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
    
    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    	ex.printStackTrace();
        return Map.of("error", "Invalid request body", "message", ex.getMessage());
    }*/

}