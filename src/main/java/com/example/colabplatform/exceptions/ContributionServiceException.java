package com.example.colabplatform.exceptions;

public class ContributionServiceException extends RuntimeException {
    public ContributionServiceException(Throwable exception){
        super(exception);
    }
    public ContributionServiceException(String message){
        super(message);
    }
}
