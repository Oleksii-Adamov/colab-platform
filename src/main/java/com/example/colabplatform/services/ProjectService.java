package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Contribution;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.infoClasses.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    public Integer create(String name, Integer creatorUserId, String projectDescription, List<Integer> tagsIds,
                          List<Integer> skillsIds) throws SQLException {
        Integer createdProjectId = DAOFactory.getInstance().getProjectDAO().create(new Project(name, creatorUserId, projectDescription),
                tagsIds, skillsIds);

        Integer creatorCollaboratorId = DAOFactory.getInstance().getCollaboratorDAO().create(new Collaborator(creatorUserId, createdProjectId));

        DAOFactory.getInstance().getCollaboratorDAO().makeAdmin(creatorCollaboratorId);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Integer month = timestamp.toLocalDateTime().getMonthValue();
        Integer year = timestamp.toLocalDateTime().getYear();
        DAOFactory.getInstance().getProjectNewUsersStatsByMonthDAO().countInUser(createdProjectId,
                month, year);

        DAOFactory.getInstance().getProjectsStatsByMonthDAO().countInCreated(month, year);

        DAOFactory.getInstance().getNewCollaboratorsStatsByMonthDAO().countInUser(month, year);

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

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Integer month = timestamp.toLocalDateTime().getMonthValue();
        Integer year = timestamp.toLocalDateTime().getYear();
        DAOFactory.getInstance().getProjectsStatsByMonthDAO().countInFinished(month, year);
    }

    public void updateProject(Integer projectId, String newName, String newProjectDescription, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException {
        DAOFactory.getInstance().getProjectDAO().updateProject(new Project(projectId, newName, newProjectDescription), newTagsIds, newSkillsIds);
    }

    public ContributionsStats getContributionStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        return DAOFactory.getInstance().getProjectContributionsStatsByMonthDAO().getStatsInRange(projectId, beginningMonth, beginningYear, endMonth, endYear);
    }

    public UsersContributionsStats getNewUsersStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        return DAOFactory.getInstance().getProjectNewUsersStatsByMonthDAO().getStatsInRange(projectId, beginningMonth, beginningYear, endMonth, endYear);
    }
    public void makeAdmin(Integer collaboratorId) throws SQLException {
        DAOFactory.getInstance().getCollaboratorDAO().makeAdmin(collaboratorId);
    }

    public void rateCollaborator(Integer collaboratorId, Integer rating) throws SQLException {
        DAOFactory.getInstance().getCollaboratorDAO().rateCollaborator(collaboratorId, rating);
    }

    public List<CollaboratorInfo> getCollaborators(Integer projectId) throws SQLException {
        List<Collaborator> collaborators = DAOFactory.getInstance().getCollaboratorDAO().getProjectCollaborators(projectId);
        return attachUserInfoToCollaborators(collaborators);
    }

    private List<CollaboratorInfo> attachUserInfoToCollaborators(List<Collaborator> collaborators) throws SQLException {
        List<CollaboratorInfo> collaboratorInfos = new ArrayList<>();
        for (Collaborator collaborator : collaborators) {
            String userFullName = DAOFactory.getInstance().getUserDAO().getFullNameById(collaborator.getUserId());
            collaboratorInfos.add(new CollaboratorInfo(userFullName, collaborator));
        }
        return collaboratorInfos;
    }

}
