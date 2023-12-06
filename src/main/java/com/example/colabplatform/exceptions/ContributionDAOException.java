package com.example.colabplatform.exceptions;

public class ContributionDAOException extends RuntimeException {
    public ContributionDAOException(Throwable exception){
        super(exception);
    }
    public ContributionDAOException(String message){
        super(message);
    }
}
