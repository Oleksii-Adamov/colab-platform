package com.example.colabplatform.validators;


import com.example.colabplatform.exceptions.UserValidatorException;

public class UserValidator {
    public void validateKeycloakId(String keycloakId) throws UserValidatorException {
        if (keycloakId == null) {
            throw new UserValidatorException("No parameter keycloakId");
        }
        if (keycloakId.length() != 36) {
            throw new UserValidatorException("Invalid parameter keycloakId");
        }
    }
    public void validateFullName(String fullName) throws UserValidatorException {
        if (fullName == null) {
            throw new UserValidatorException("No parameter fullName");
        }
    }
}
