package com.example.colabplatform.validators;

import com.example.colabplatform.exceptions.ValidationCommonsException;


public class ProjectValidator {
    public Integer getValidatedProjectId(String projectIdStr) throws ValidationCommonsException {
        return ValidationCommons.getValidatedPositiveIntegerField(projectIdStr, "projectId");
    }

    public void validateName(String name) throws ValidationCommonsException {
        ValidationCommons.validateNotNullField(name, "name");
    }

}
