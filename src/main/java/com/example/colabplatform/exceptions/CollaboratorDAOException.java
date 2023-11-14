package com.example.colabplatform.exceptions;

public class CollaboratorDAOException extends RuntimeException {
    public CollaboratorDAOException(Throwable exception){
        super(exception);
    }
    public CollaboratorDAOException(String message){
        super(message);
    }
}
