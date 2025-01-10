package com.simbrella.dev.loan_mgt_service.exception;

import com.simbrella.dev.loan_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.loan_mgt_service.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>(false, "Validation Failed", errors), NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getAllErrors().forEach(violation -> {
            String propertyPath = violation.getCode();
            String message = violation.getDefaultMessage();
            errors.put(propertyPath, message);

        });
        return new ResponseEntity<>(new ApiResponse<>(false, "Input not Valid", errors), BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    @ExceptionHandler(LoanAlreadyExistException.class)
    public ResponseEntity<ApiResponse<String>> handleLoanAlreadyExistException(LoanAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleLoanNotFoundException(LoanNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomValidationException(CustomValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Map<String, String>>> handleRequestPathVariableException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("parameter", String.valueOf(ex.getParameter()));
        if (ex.getValue() != null) {
            errors.put("value", ex.getValue().toString());
        }
        errors.put("requiredType", Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        String message = String.format("Request argument type mismatch. Expected: %s, Actual: %s",
                ex.getRequiredType().getSimpleName(), ex.getValue());
        return new ResponseEntity<>(new ApiResponse<>(false, message, errors), HttpStatus.BAD_REQUEST);
    }



}
