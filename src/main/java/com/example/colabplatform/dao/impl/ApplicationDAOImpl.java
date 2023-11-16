package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ApplicationDAO;
import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.exceptions.ApplicationDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAOImpl implements ApplicationDAO {
    @Override
    public Integer create(Application application) throws SQLException {
        String[] returnCols = { "APPLICATIONID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO PROJECTAPPLICANTS (USERID, PROJECTID, APPLICATIONDATE, STATUS)" +
                                " VALUES(?, ?, ?, ?)",
                        returnCols);
        preparedStatement.setInt(1, application.getUserId());
        preparedStatement.setInt(2, application.getProjectId());
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setString(4, application.getStatus());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        ConnectionFactory.instance().releaseConnection();
        int id;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        else {
            throw new ApplicationDAOException("Didn't get created Application id");
        }
        return id;
    }

    @Override
    public void approve(Integer applicationId, Integer userId, Integer projectId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE PROJECTAPPLICANTS SET Status = ? WHERE APPLICATIONID = ?");

        preparedStatement.setString(1, Application.Status.APPROVED.toString()); // Replace with the status you want
        preparedStatement.setInt(2, applicationId); // Replace with your application ID

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new ApplicationDAOException("Application approval update failed");
        }
        DAOFactory.getInstance().getCollaboratorDAO().create(new Collaborator(userId, projectId));
    }

    @Override
    public void reject(Integer applicationId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE PROJECTAPPLICANTS SET Status = ? WHERE APPLICATIONID = ?");

        preparedStatement.setString(1, Application.Status.REJECTED.toString()); // Replace with the status you want
        preparedStatement.setInt(2, applicationId); // Replace with your application ID

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new ApplicationDAOException("Application rejection update failed");
        }
    }

    @Override
    public List<Application> getProjectPendingApplications(Integer projectId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT ap.ApplicationID, ap.UserID, ap.ProjectID, ap.ApplicationDate, ap.Status" +
                        " FROM PROJECTAPPLICANTS ap" +
                        " WHERE ap.ProjectID = ? AND ap.STATUS = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setString(2, Application.Status.PENDING.toString());
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        return applicationsFromResultSet(rs);
    }

    private List<Application> applicationsFromResultSet(ResultSet rs) throws SQLException {
        List<Application> applications = new ArrayList<>();
        while (rs.next()) {
            Application application = new Application();
            application.setId(rs.getInt(1));
            application.setUserId(rs.getInt(2));
            application.setProjectId(rs.getInt(3));
            Timestamp timestamp = rs.getTimestamp(4);
            application.setDay(timestamp.getDay());
            application.setMonth(timestamp.getMonth());
            application.setYear(timestamp.getYear());
            application.setStatus(Application.Status.valueOf(rs.getString(5)));
            applications.add(application);
        }
        return applications;
    }
}
