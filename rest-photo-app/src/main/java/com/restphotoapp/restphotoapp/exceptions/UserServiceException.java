package com.restphotoapp.restphotoapp.exceptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserServiceException(String message) {
        super(message);
    }

}