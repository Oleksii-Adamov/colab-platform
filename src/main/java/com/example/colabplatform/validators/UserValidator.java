package com.example.colabplatform.validators;


import com.example.colabplatform.exceptions.UserValidatorException;

public class UserValidator {

    public Integer getValidatedUserId(String userIdStr) throws UserValidatorException {
        if (userIdStr == null) {
            throw new UserValidatorException("No parameter userId");
        }
        Integer userId;
        try {
            userId = Integer.parseInt(userIdStr);
            if (userId <= 0) throw new UserValidatorException("Parameter userId <= 0");
        } catch (NumberFormatException e) {
            throw new UserValidatorException("Non integer parameter userId");
        }
        return userId;
    }
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
