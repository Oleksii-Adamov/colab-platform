package com.example.colabplatform.infoClasses;

import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Contribution;

public class CollaboratorInfo {
    private String userFullName;
    private Collaborator collaborator;

    public CollaboratorInfo() {
    }

    public CollaboratorInfo(String userFullName, Collaborator collaborator) {
        this.userFullName = userFullName;
        this.collaborator = collaborator;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }
}
