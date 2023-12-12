package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.enitities.Skill;
import com.example.colabplatform.enitities.Tag;
import com.example.colabplatform.exceptions.ApplicationDAOException;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;

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

    @Override
    public List<CollaboratorProjectInfo> getProjectsUserIn(Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName,cb.ISADMIN FROM Projects pr" +
                        " INNER JOIN PROJECTCOLLABORATORS cb on pr.PROJECTID = cb.PROJECTID" +
                        " WHERE cb.USERID = ?" +
                        " ORDER BY cb.ISADMIN DESC");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        List<CollaboratorProjectInfo> projects = new ArrayList<>();
        while (rs.next()) {
            CollaboratorProjectInfo collaboratorProjectInfo = new CollaboratorProjectInfo();
            collaboratorProjectInfo.setProject(projectShortInfoFromResultSet(rs));
            collaboratorProjectInfo.setAdmin(rs.getInt(3));
            projects.add(collaboratorProjectInfo);
        }
        return projects;
    }

    @Override
    public List<Project> getProjectsCreatedByUser(Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName FROM Projects pr WHERE pr.CREATORUSERID = ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        return projectsFromResultSet(rs);
    }

    private List<Project> projectsFromResultSet(ResultSet rs) throws SQLException {
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            projects.add(projectShortInfoFromResultSet(rs));
        }
        return projects;
    }

    private Project projectShortInfoFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt(1));
        project.setName(rs.getString(2));
        return project;
    }


    @Override
    public Project getProjectInfo(Integer projectId) throws SQLException {
        Project project = new Project();
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT P.ProjectName, P.PROJECTDESCRIPTION, P.RATING, P.NUMBEROFRATINGS" +
                        " FROM Projects P" +
                        " WHERE P.PROJECTID = ?");
        preparedStatement.setInt(1, projectId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            project.setName(rs.getString("ProjectName"));
            project.setDescription(rs.getString("PROJECTDESCRIPTION"));
            project.setRating(rs.getFloat("RATING"));
            project.setNumberOfRatings(rs.getInt("NUMBEROFRATINGS"));
        }

        preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT T.TAGID, T.TAGNAME" +
                        " FROM Projects P" +
                        " INNER JOIN PROJECTTAGSASSIGNMENTS PT on P.PROJECTID = PT.PROJECTID" +
                        " INNER JOIN TAGS T on PT.TAGID = T.TAGID" +
                        " WHERE P.PROJECTID = ?");
        preparedStatement.setInt(1, projectId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            project.getTags().add(new Tag(rs.getInt("TAGID"),rs.getString("TAGNAME")));
        }
        if (project.getName() == null) return null;

        preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT S.SKILLID, S.SKILLNAME" +
                        " FROM Projects P" +
                        " INNER JOIN PROJECTSKILLASSIGNMENTS PS on P.PROJECTID = PS.PROJECTID" +
                        " INNER JOIN SKILLS S on S.SKILLID = PS.SKILLID" +
                        " WHERE P.PROJECTID = ?");
        preparedStatement.setInt(1, projectId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            project.getSkills().add(new Skill(rs.getInt("SKILLID"), rs.getString("SKILLNAME")));
        }

        ConnectionFactory.instance().releaseConnection();
        return project;
    }
    @Override
    public void rateProject(Integer projectId, Integer userId, Integer rating) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE Projects P" +
                        " SET P.rating = (P.rating * P.NUMBEROFRATINGS + ?) / (P.NUMBEROFRATINGS + 1)," +
                        " P.NUMBEROFRATINGS = P.NUMBEROFRATINGS + 1" +
                        " WHERE P.ProjectID = ?");

        preparedStatement.setInt(1, rating);
        preparedStatement.setInt(2, projectId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new ApplicationDAOException("Project rating update failed");
        }
        // create new rating or update if exists
        preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("MERGE INTO PROJECTRATINGS PR INNER JOIN Projects P ON PR.PROJECTID = P.PROJECTID" +
                        " USING DUAL" +
                        " ON (PR.PROJECTID = ? AND PR.USERID = ?)" +
                        " WHEN MATCHED THEN" +
                        " UPDATE SET P.RATING = (P.RATING * P.NUMBEROFRATINGS - PR.RATING + ?) / P.NUMBEROFRATINGS, PR.RATING = ?" +
                        " WHEN NOT MATCHED THEN" +
                        " INSERT (PR.PROJECTID, PR.USERID, PR.RATING)" +
                        " VALUES (?, ?, ?)");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, userId);
        preparedStatement.setInt(3, rating);
        preparedStatement.setInt(4, rating);
        preparedStatement.setInt(4, projectId);
        preparedStatement.setInt(5, userId);
        preparedStatement.setDouble(6, rating);
        preparedStatement.executeUpdate();
    }
}
