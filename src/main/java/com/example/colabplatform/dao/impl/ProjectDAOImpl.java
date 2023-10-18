package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Project;

import java.net.ConnectException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    @Override
    public List<Project> getProjects() throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName FROM Projects pr");
        ResultSet rs = preparedStatement.executeQuery();
        return projectsFromResultSet(rs);
    }

    private List<Project> projectsFromResultSet(ResultSet rs) throws SQLException {
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            Project project = new Project();
            project.setId(rs.getInt(1));
            project.setName(rs.getString(2));
            projects.add(project);
        }
        return projects;
    }
}
