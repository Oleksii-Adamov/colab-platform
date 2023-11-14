package com.example.colabplatform.exceptions;

public class ApplicationDAOException extends RuntimeException {
    public ApplicationDAOException(Throwable exception){
        super(exception);
    }
    public ApplicationDAOException(String message){
        super(message);
    }
}
