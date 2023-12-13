package com.example.colabplatform.exceptions;

public class ProjectException extends RuntimeException {
    public ProjectException(Throwable exception){
        super(exception);
    }
    public ProjectException(String message){
        super(message);
    }
}
