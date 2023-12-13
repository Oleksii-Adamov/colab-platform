package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.*;
import com.example.colabplatform.exceptions.ApplicationDAOException;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;

import java.sql.*;
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
                "SELECT pr.ProjectID, pr.ProjectName, pr.ISFINISHED FROM Projects pr");
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        return projectsFromResultSet(rs);
    }

    @Override
    public List<CollaboratorProjectInfo> getProjectsUserIn(Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName, pr.ISFINISHED, cb.ISADMIN, cb.DATEOFJOINING, cb.RATING," +
                        " cb.NUMBEROFCONTRIBUTIONS, cb.TOTALVALUE FROM Projects pr" +
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

            Collaborator collaborator = new Collaborator();
            collaborator.setUserId(userId);
            collaborator.setAdmin(rs.getInt(4));
            Timestamp timestamp = rs.getTimestamp(5);
            collaborator.setDayOfJoining(timestamp.toLocalDateTime().getDayOfMonth());
            collaborator.setMonthOfJoining(timestamp.toLocalDateTime().getMonthValue());
            collaborator.setYearOfJoining(timestamp.toLocalDateTime().getYear());
            collaborator.setRating(rs.getInt(6));
            collaborator.setNumberOfContributions(rs.getInt(7));
            collaborator.setTotalValue(rs.getInt(8));
            collaboratorProjectInfo.setCollaborator(collaborator);

            projects.add(collaboratorProjectInfo);
        }
        return projects;
    }

    @Override
    public List<Project> getProjectsCreatedByUser(Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT pr.ProjectID, pr.ProjectName, pr.ISFINISHED FROM Projects pr WHERE pr.CREATORUSERID = ?");
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
        project.setFinished(rs.getInt(3));
        return project;
    }


    @Override
    public Project getProjectInfo(Integer projectId) throws SQLException {
        Project project = new Project();
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT P.ProjectName, P.PROJECTDESCRIPTION, P.RATING, P.NUMBEROFRATINGS, P.ISFINISHED" +
                        " FROM Projects P" +
                        " WHERE P.PROJECTID = ?");
        preparedStatement.setInt(1, projectId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            project.setName(rs.getString("ProjectName"));
            project.setDescription(rs.getString("PROJECTDESCRIPTION"));
            project.setRating(rs.getFloat("RATING"));
            project.setNumberOfRatings(rs.getInt("NUMBEROFRATINGS"));
            project.setFinished(rs.getInt("ISFINISHED"));
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
        // check if rating of this user already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT PR.RATING FROM PROJECTRATINGS PR" +
                        " WHERE PR.ProjectID = ? AND PR.USERID = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, userId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevRating = rs.getInt(1);
            // update rating entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE PROJECTRATINGS PR" +
                            " SET PR.RATING = ?" +
                            " WHERE PR.ProjectID = ? AND PR.USERID = ?");
            preparedStatement.setInt(1, rating);
            preparedStatement.setInt(2, projectId);
            preparedStatement.setInt(3, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("Project rating update failed");
            }
            // update average rating
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE Projects P" +
                            " SET P.rating = (P.rating * P.NUMBEROFRATINGS + ?) / P.NUMBEROFRATINGS" +
                            " WHERE P.ProjectID = ?");
            preparedStatement.setInt(1, rating - prevRating);
            preparedStatement.setInt(2, projectId);
            rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("Project average rating update failed");
            }
        }
        else {
            // create new entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("INSERT INTO PROJECTRATINGS (ProjectID, USERID, RATING)" +
                            " VALUES(?, ?, ?)");
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, rating);
            preparedStatement.executeUpdate();
            // update average rating and count
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE Projects P" +
                            " SET P.rating = (P.rating * P.NUMBEROFRATINGS + ?) / (P.NUMBEROFRATINGS + 1)," +
                            " P.NUMBEROFRATINGS = P.NUMBEROFRATINGS + 1" +
                            " WHERE P.ProjectID = ?");
            preparedStatement.setInt(1, rating);
            preparedStatement.setInt(2, projectId);

            int rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("Project average rating update failed");
            }
        }
    }

    @Override
    public Integer getUserRating(Integer projectId, Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT PR.RATING FROM PROJECTRATINGS PR" +
                        " WHERE PR.ProjectID = ? AND PR.USERID = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, userId);
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        if (rs.next()) {
            Integer rating = rs.getInt(1);
            return rating;
        }
        else {
            return null;
        }
    }
    @Override
    public void markAsFinished(Integer projectId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE Projects P" +
                        " SET P.ISFINISHED = 1" +
                        " WHERE P.ProjectID = ?");
        preparedStatement.setInt(1, projectId);
        Integer rowsAffected = preparedStatement.executeUpdate();
        ConnectionFactory.instance().releaseConnection();
        if (rowsAffected < 1) {
            throw new ProjectDAOException("Project markAsFinished update failed");
        }
    }
    @Override
    public void updateProject(Project newProjectInfo, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("UPDATE Projects P" +
                                " SET P.PROJECTNAME = ?, P.PROJECTDESCRIPTION = ?" +
                                " WHERE P.ProjectID = ?");
        preparedStatement.setString(1, newProjectInfo.getName());
        preparedStatement.setString(2, newProjectInfo.getDescription());
        preparedStatement.setInt(3, newProjectInfo.getId());
        Integer rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected < 1) {
            throw new ProjectDAOException("Project update failed");
        }

        PreparedStatement deleteStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "DELETE FROM PROJECTTAGSASSIGNMENTS WHERE PROJECTID = ?");
        deleteStatement.setInt(1, newProjectInfo.getId());
        deleteStatement.executeUpdate();
        PreparedStatement insertStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTTAGSASSIGNMENTS (PROJECTID, TAGID) VALUES(?, ?)");
        for (int tagId : newTagsIds) {
            insertStatement.setInt(1, newProjectInfo.getId());
            insertStatement.setInt(2, tagId);
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();

        deleteStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "DELETE FROM PROJECTSKILLASSIGNMENTS WHERE PROJECTID = ?");
        deleteStatement.setInt(1, newProjectInfo.getId());
        deleteStatement.executeUpdate();
        insertStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTSKILLASSIGNMENTS (PROJECTID, SKILLID) VALUES(?, ?)");
        for (int skillId : newSkillsIds) {
            insertStatement.setInt(1, newProjectInfo.getId());
            insertStatement.setInt(2, skillId);
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
        ConnectionFactory.instance().releaseConnection();
    }
}
