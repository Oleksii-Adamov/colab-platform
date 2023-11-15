package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.CollaboratorDAO;
import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.exceptions.ApplicationDAOException;
import com.example.colabplatform.exceptions.CollaboratorDAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollaboratorDAOImpl implements CollaboratorDAO {
    @Override
    public Integer create(Collaborator collaborator) throws SQLException {
        String[] returnCols = { "COLLABORATORID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTCOLLABORATORS (USERID, PROJECTID, ISADMIN , DATEOFJOINING, RATING)" +
                                " VALUES(?, ?, ?, ?, ?)",
                        returnCols);
        preparedStatement.setInt(1, collaborator.getUserId());
        preparedStatement.setInt(2, collaborator.getProjectId());
        preparedStatement.setInt(3, 0);
        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setNull(5, Types.NUMERIC);
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        ConnectionFactory.instance().releaseConnection();
        int id;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        else {
            throw new CollaboratorDAOException("Didn't get created Collaborator id");
        }
        return id;
    }

    @Override
    public List<Collaborator> getProjectCollaborators(Integer projectId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT c.COLLABORATORID, c.UserID, c.ProjectID, c.ISADMIN, c.DATEOFJOINING, c.RATING" +
                        " FROM PROJECTCOLLABORATORS c WHERE c.ProjectID = ?");
        preparedStatement.setInt(1, projectId);
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        return collaboratorsFromResultSet(rs);
    }

    @Override
    public void makeAdmin(Integer collaboratorId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE PROJECTCOLLABORATORS SET ISADMIN = ? WHERE COLLABORATORID = ?");

        preparedStatement.setInt(1, 1); // Replace with the status you want
        preparedStatement.setInt(2, collaboratorId); // Replace with your aApplication ID

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new CollaboratorDAOException("Making user admin of project failed");
        }
    }

    @Override
    public Collaborator getByUserAndProjectId(Integer userId, Integer projectId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT c.COLLABORATORID, c.UserID, c.ProjectID, c.ISADMIN, c.DATEOFJOINING, c.RATING" +
                        " FROM PROJECTCOLLABORATORS c WHERE c.USERID = ? AND c.ProjectID = ?");
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, projectId);
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        if (rs.next()) {
            return collaboratorFromResultSet(rs);
        }
        else {
            return null;
        }
    }

    private List<Collaborator> collaboratorsFromResultSet(ResultSet rs) throws SQLException {
        List<Collaborator> collaborators = new ArrayList<>();
        while (rs.next()) {
            collaborators.add(collaboratorFromResultSet(rs));
        }
        return collaborators;
    }

    private Collaborator collaboratorFromResultSet(ResultSet rs) throws SQLException {
        Collaborator collaborator = new Collaborator();
        collaborator.setId(rs.getInt(1));
        collaborator.setUserId(rs.getInt(2));
        collaborator.setProjectId(rs.getInt(3));
        collaborator.setAdmin(rs.getInt(4));
        collaborator.setDateOfJoining(rs.getTimestamp(5).toLocalDateTime().toLocalDate());
        collaborator.setRating(rs.getInt(6));
        return collaborator;
    }
}
