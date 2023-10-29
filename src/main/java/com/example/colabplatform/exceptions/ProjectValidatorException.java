package com.example.colabplatform.exceptions;

public class ProjectValidatorException extends RuntimeException {
    public ProjectValidatorException(Throwable exception){
        super(exception);
    }
    public ProjectValidatorException(String message){
        super(message);
    }
}
