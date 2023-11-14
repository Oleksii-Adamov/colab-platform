package com.example.colabplatform.exceptions;

public class CollaboratorException extends RuntimeException {
    public CollaboratorException(Throwable exception){
        super(exception);
    }
    public CollaboratorException(String message){
        super(message);
    }
}
