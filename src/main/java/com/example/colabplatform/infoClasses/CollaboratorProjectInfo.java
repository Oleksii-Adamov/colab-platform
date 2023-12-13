package com.example.colabplatform.infoClasses;

import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Project;

public class CollaboratorProjectInfo {
    private Collaborator collaborator;
    private Project project;

    public CollaboratorProjectInfo() {
    }

    public CollaboratorProjectInfo(Collaborator collaborator, Project project) {
        this.collaborator = collaborator;
        this.project = project;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
