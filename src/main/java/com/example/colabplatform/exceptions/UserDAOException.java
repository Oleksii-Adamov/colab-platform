package com.example.colabplatform.exceptions;

public class UserDAOException extends RuntimeException {
    public UserDAOException(Throwable exception){
        super(exception);
    }
    public UserDAOException(String message){
        super(message);
    }
}
