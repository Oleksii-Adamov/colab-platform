package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Collaborator;

import java.sql.SQLException;
import java.util.List;

public interface CollaboratorDAO {
    Integer create(Collaborator collaborator) throws SQLException;
    List<Collaborator> getProjectCollaborators(Integer projectId) throws SQLException;

    void makeAdmin(Integer collaboratorId) throws SQLException;

    Collaborator getByUserAndProjectId(Integer userId, Integer projectId) throws SQLException;

    void rateCollaborator(Integer collaboratorId, Integer rating) throws SQLException;
}
