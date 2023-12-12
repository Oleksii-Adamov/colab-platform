package com.example.colabplatform.validators;

import com.example.colabplatform.exceptions.ProjectValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;


public class ProjectValidator {
    public Integer getValidatedProjectId(String projectIdStr) throws ValidationCommonsException {
        return ValidationCommons.getValidatedPositiveIntegerField(projectIdStr, "projectId");
    }

    public void validateName(String name) throws ValidationCommonsException {
        ValidationCommons.validateNotNullField(name, "name");
    }

    public Integer getValidatedProjectRating(String ratingStr) throws ValidationCommonsException {
        Integer rating = ValidationCommons.getValidatedPositiveIntegerField(ratingStr, "rating");
        if (rating < 1 || rating > 5) {
            throw new ProjectValidatorException("rating is not between 1 and 5");
        }
        return rating;
    }
}
