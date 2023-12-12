package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {
    Integer create(Project project, List<Integer> tagsIds, List<Integer> skillsIds) throws SQLException;
    List<Project> getProjects() throws SQLException;

    List<Project> getProjectsCreatedByUser(Integer userId) throws SQLException;

    Project getProjectInfo(Integer projectId) throws SQLException;

    List<CollaboratorProjectInfo> getProjectsUserIn(Integer userId) throws SQLException;

    void rateProject(Integer projectId, Integer userId, Integer rating) throws SQLException;
}
