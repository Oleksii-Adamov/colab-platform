package com.example.colabplatform.exceptions;

public class ProjectDAOException extends RuntimeException {
    public ProjectDAOException(Throwable exception){
        super(exception);
    }
    public ProjectDAOException(String message){
        super(message);
    }
}
