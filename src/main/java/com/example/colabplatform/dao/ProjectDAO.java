package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {
    Integer create(Project project, List<Integer> tagsIds, List<Integer> skillsIds) throws SQLException;
    List<Project> getProjects() throws SQLException;

    List<Project> getProjectsCreatedByUser(Integer userId) throws SQLException;
}
