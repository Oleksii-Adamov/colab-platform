package com.example.colabplatform.validators;
import com.example.colabplatform.exceptions.ValidationCommonsException;

public class ContributionValidator {
    public Integer getValidatedContributionId(String contributionIdStr) throws ValidationCommonsException {
        return ValidationCommons.getValidatedPositiveIntegerField(contributionIdStr, "contributionId");
    }
}
