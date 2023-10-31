package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.ProjectDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    @Override
    public Integer create(Project project) throws SQLException {
        String[] returnCols = { "PROJECTID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTS (PROJECTNAME, PROJECTDESCRIPTION, CREATORUSERID) VALUES(?, null, ?)",
                        returnCols);
        preparedStatement.setString(1, project.getName());
        preparedStatement.setInt(2, project.getCreatorUserID());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        ConnectionFactory.instance().releaseConnection();
        if (rs.next()) {
            return rs.getInt(1);
        }
        else {
            throw new ProjectDAOException("Didn't get created project id");
        }
    }

    @Override
    public List<Project> getProjects() throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName FROM Projects pr");
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
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
