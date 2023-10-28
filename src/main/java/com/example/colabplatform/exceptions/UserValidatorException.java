package com.example.colabplatform.exceptions;

public class UserValidatorException extends RuntimeException {
    public UserValidatorException(Throwable exception){
        super(exception);
    }
    public UserValidatorException(String message){
        super(message);
    }
}
