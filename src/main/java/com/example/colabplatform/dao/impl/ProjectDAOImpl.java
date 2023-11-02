package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.ProjectDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    @Override
    public Integer create(Project project, List<Integer> tagsIds, List<Integer> skillsIds) throws SQLException {
        String[] returnCols = { "PROJECTID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTS (PROJECTNAME, PROJECTDESCRIPTION, CREATORUSERID, CREATIONTIMESTAMP)" +
                                " VALUES(?, ?, ?, ?)",
                        returnCols);
        preparedStatement.setString(1, project.getName());
        preparedStatement.setString(2, project.getDescription());
        preparedStatement.setInt(3, project.getCreatorUserID());
        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        int projectId;
        if (rs.next()) {
            projectId = rs.getInt(1);
        }
        else {
            throw new ProjectDAOException("Didn't get created project id");
        }
        preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTTAGSASSIGNMENTS (PROJECTID, TAGID) VALUES(?, ?)");
        for (int tagId : tagsIds) {
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, tagId);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTSKILLASSIGNMENTS (PROJECTID, SKILLID) VALUES(?, ?)");
        for (int skillId : skillsIds) {
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, skillId);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        ConnectionFactory.instance().releaseConnection();
        return projectId;
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
