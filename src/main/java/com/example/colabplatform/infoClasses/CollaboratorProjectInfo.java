package com.example.colabplatform.infoClasses;

import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.CollaboratorException;

public class CollaboratorProjectInfo {
    private boolean isAdmin;
    private Project project;

    public CollaboratorProjectInfo() {
    }

    public CollaboratorProjectInfo(boolean isAdmin, Project project) {
        this.isAdmin = isAdmin;
        this.project = project;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setAdmin(Integer admin) {
        if (admin == 1) {
            this.isAdmin = true;
        }
        else if (admin == 0) {
            this.isAdmin = false;
        }
        else {
            throw new CollaboratorException("Invalid argument admin, must be 0 or 1");
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
