package com.example.colabplatform.exceptions;

public class ValidationCommonsException extends RuntimeException {
    public ValidationCommonsException(Throwable exception){
        super(exception);
    }
    public ValidationCommonsException(String message){
        super(message);
    }
}
