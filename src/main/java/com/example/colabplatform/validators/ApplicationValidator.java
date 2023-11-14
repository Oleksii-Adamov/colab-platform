package com.example.colabplatform.validators;
import com.example.colabplatform.exceptions.ValidationCommonsException;

public class ApplicationValidator {
    public Integer getValidatedApplicationId(String applicationIdStr) throws ValidationCommonsException {
        return ValidationCommons.getValidatedPositiveIntegerField(applicationIdStr, "applicationId");
    }
}
