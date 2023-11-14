package com.example.colabplatform.validators;


import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;

public class UserValidator {

    public Integer getValidatedUserId(String userIdStr) throws ValidationCommonsException {
        return ValidationCommons.getValidatedPositiveIntegerField(userIdStr, "userId");
    }
    public void validateKeycloakId(String keycloakId) throws UserValidatorException {
        if (keycloakId == null) {
            throw new UserValidatorException("No parameter keycloakId");
        }
        if (keycloakId.length() != 36) {
            throw new UserValidatorException("Invalid parameter keycloakId");
        }
    }
    public void validateFullName(String fullName) throws ValidationCommonsException {
        ValidationCommons.validateNotNullField(fullName, "fullName");
    }
}
