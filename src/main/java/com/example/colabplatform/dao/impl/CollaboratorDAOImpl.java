package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.CollaboratorDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Collaborator;
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

    private List<Collaborator> collaboratorsFromResultSet(ResultSet rs) throws SQLException {
        List<Collaborator> collaborators = new ArrayList<>();
        while (rs.next()) {
            Collaborator collaborator = new Collaborator();
            collaborator.setId(rs.getInt(1));
            collaborator.setUserId(rs.getInt(2));
            collaborator.setProjectId(rs.getInt(3));
            collaborator.setAdmin(rs.getInt(4));
            collaborator.setDateOfJoining(rs.getTimestamp(5).toLocalDateTime().toLocalDate());
            collaborator.setRating(rs.getInt(6));
            collaborators.add(collaborator);
        }
        return collaborators;
    }
}
