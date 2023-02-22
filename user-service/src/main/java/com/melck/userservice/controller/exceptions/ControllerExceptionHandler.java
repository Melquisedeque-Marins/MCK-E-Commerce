package com.melck.userservice.controller.exceptions;

import com.melck.userservice.service.exception.CpfAlreadyInUseException;
import com.melck.userservice.service.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    Instant now = Instant.now();
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(UserNotFoundException e, HttpServletRequest request){

        StandardError error = new StandardError(now, HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CpfAlreadyInUseException.class)
    public ResponseEntity<StandardError> cpfAlreadyInUseException(CpfAlreadyInUseException e, HttpServletRequest request){

        StandardError error = new StandardError(now, HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationException(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError error = new ValidationError(now, HttpStatus.BAD_REQUEST.value(), request.getRequestURI() , "Field validation error - please check the fields above");

        for (FieldError x : e.getBindingResult().getFieldErrors()){
            error.addErrors(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
