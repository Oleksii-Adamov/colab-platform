package com.example.colabplatform.validators;

import com.example.colabplatform.exceptions.ProjectValidatorException;


public class ProjectValidator {
    public Integer getValidatedProjectId(String projectIdStr) throws ProjectValidatorException {
        if (projectIdStr == null) {
            throw new ProjectValidatorException("No parameter projectId");
        }
        Integer projectId;
        try {
            projectId = Integer.parseInt(projectIdStr);
            if (projectId <= 0) throw new ProjectValidatorException("Parameter projectId <= 0");
        } catch (NumberFormatException e) {
            throw new ProjectValidatorException("Non integer parameter projectId");
        }
        return projectId;
    }

    public void validateName(String name) throws ProjectValidatorException {
        if (name == null) {
            throw new ProjectValidatorException("No parameter name");
        }
    }

}
