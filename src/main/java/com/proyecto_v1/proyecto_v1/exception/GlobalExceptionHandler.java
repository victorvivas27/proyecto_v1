package com.proyecto_v1.proyecto_v1.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("message", "Recurso no encontrado: " + exception.getMessage());
        return exceptionMessage;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, Object> exceptionMessage = new HashMap<>();
        exceptionMessage.put("message", "Solicitud incorrecta. Revise los campos.");
        exceptionMessage.put("errors", getValidationErrors(exception));
        return exceptionMessage;
    }

    private Map<String, String> getValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleJsonParseException(JsonParseException exception) {
        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("message", "Error al analizar JSON: " + exception.getMessage());
        return exceptionMessage;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception exception) {
        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("message", "Error interno del servidor: " + exception.getMessage());
        return exceptionMessage;
    }
}