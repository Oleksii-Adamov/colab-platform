package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Project;

import java.sql.SQLException;
import java.util.List;

public class ProjectService {
    public Integer create(String name, Integer creatorUserId, String projectDescription, List<Integer> tagsIds,
                          List<Integer> skillsIds) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().create(new Project(name, creatorUserId, projectDescription),
                tagsIds, skillsIds);
    }

    public List<Project> getProjectsCreatedByUser(Integer userId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getProjectsCreatedByUser(userId);
    }

    public Project getProjectInfo(Integer projectId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getProjectInfo(projectId);
    }
}
