package com.restphotoapp.restphotoapp.exceptions;

import java.util.Date;

import com.restphotoapp.restphotoapp.models.DTO.ErrorMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = { UserServiceException.class })
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(value = { Exception.class })
    // public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest
    // request) {
    // ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
    // return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}