package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;

import java.sql.SQLException;
import java.util.List;

public class ProjectService {
    public Integer create(String name, Integer creatorUserId, String projectDescription, List<Integer> tagsIds,
                          List<Integer> skillsIds) throws SQLException {
        Integer createdProjectId = DAOFactory.getInstance().getProjectDAO().create(new Project(name, creatorUserId, projectDescription),
                tagsIds, skillsIds);
        Integer creatorCollaboratorId = DAOFactory.getInstance().getCollaboratorDAO().create(new Collaborator(creatorUserId, createdProjectId));
        DAOFactory.getInstance().getCollaboratorDAO().makeAdmin(creatorCollaboratorId);
        return  createdProjectId;
    }

    public List<CollaboratorProjectInfo> getProjectsUserIn(Integer userId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getProjectsUserIn(userId);
    }

    public Project getProjectInfo(Integer projectId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getProjectInfo(projectId);
    }

    public Collaborator getCollaborator(Integer userId, Integer projectId) throws SQLException {
        return DAOFactory.getInstance().getCollaboratorDAO().getByUserAndProjectId(userId, projectId);
    }

    public List<Project> getRecommendedProjects(Integer userId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getProjects();
    }

    public void rateProject(Integer projectId, Integer userId, Integer rating) throws SQLException {
        DAOFactory.getInstance().getProjectDAO().rateProject(projectId, userId, rating);
    }

    public Integer getUserRating(Integer projectId, Integer userId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().getUserRating(projectId, userId);
    }

    public void markAsFinished(Integer projectId) throws SQLException {
        DAOFactory.getInstance().getProjectDAO().markAsFinished(projectId);
    }

    public void updateProject(Integer projectId, String newName, String newProjectDescription, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException {
        DAOFactory.getInstance().getProjectDAO().updateProject(new Project(projectId, newName, newProjectDescription), newTagsIds, newSkillsIds);
    }
}
