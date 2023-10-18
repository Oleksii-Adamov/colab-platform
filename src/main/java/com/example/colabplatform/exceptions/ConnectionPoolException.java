package com.example.colabplatform.exceptions;

public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException(Throwable exception){
        super(exception);
    }
    public ConnectionPoolException(String message){
        super(message);
    }
}
