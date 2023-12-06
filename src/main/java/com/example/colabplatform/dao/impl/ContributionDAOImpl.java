package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ContributionDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Contribution;
import com.example.colabplatform.exceptions.ContributionDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ContributionDAOImpl implements ContributionDAO {
    @Override
    public Integer create(Contribution contribution) throws SQLException {
        String[] returnCols = { "APPLICATIONID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO CONTRIBUTIONS (USERID, PROJECTID, CONTRIBUTIONDESCRIPTION, CONTRIBUTIONDATE, CONTRIBUTIONVALUE, STATUS)" +
                                " VALUES(?, ?, ?, ?, ?, ?)",
                        returnCols);
        preparedStatement.setInt(1, contribution.getUserId());
        preparedStatement.setInt(2, contribution.getProjectId());
        preparedStatement.setString(3, contribution.getDescription());
        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setInt(5, contribution.getValue());
        preparedStatement.setString(6, contribution.getStatus());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        ConnectionFactory.instance().releaseConnection();
        int id;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        else {
            throw new ContributionDAOException("Didn't get created Contribution id");
        }
        return id;
    }

    @Override
    public void approve(Integer contributionId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE CONTRIBUTIONS SET STATUS = ? WHERE CONTRIBUTIONID = ?");

        preparedStatement.setString(1, Contribution.Status.APPROVED.toString());
        preparedStatement.setInt(2, contributionId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new ContributionDAOException("Contribution approval update failed");
        }
    }

    @Override
    public void reject(Integer contributionId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("UPDATE CONTRIBUTIONS SET Status = ? WHERE CONTRIBUTIONID = ?");

        preparedStatement.setString(1, Contribution.Status.REJECTED.toString());
        preparedStatement.setInt(2, contributionId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected < 1) {
            throw new ContributionDAOException("Contribution rejection update failed");
        }
    }

    @Override
    public List<Contribution> getProjectPendingContributions(Integer projectId) throws SQLException {
        return getProjectContributionsWithStatus(projectId, Contribution.Status.PENDING);
    }

    @Override
    public List<Contribution> getProjectApprovedContributions(Integer projectId) throws SQLException {
        return getProjectContributionsWithStatus(projectId, Contribution.Status.APPROVED);
    }

    private List<Contribution> getProjectContributionsWithStatus(Integer projectId, Contribution.Status status) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT c.CONTRIBUTIONID, c.UserID, c.ProjectID, c.CONTRIBUTIONDESCRIPTION, c.CONTRIBUTIONDATE, c.CONTRIBUTIONVALUE, c.Status" +
                        " FROM CONTRIBUTIONS c" +
                        " WHERE c.ProjectID = ? AND c.STATUS = ?" +
                        " ORDER BY c.CONTRIBUTIONDATE DESC");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setString(2, status.toString());
        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        return contributionsFromResultSet(rs, false);
    }

    private List<Contribution> contributionsFromResultSet(ResultSet rs, boolean expectApprovalDate) throws SQLException {
        List<Contribution> contributions = new ArrayList<>();
        while (rs.next()) {
            Contribution contribution = new Contribution();
            contribution.setId(rs.getInt(1));
            contribution.setUserId(rs.getInt(2));
            contribution.setProjectId(rs.getInt(3));
            contribution.setDescription(rs.getString(4));
            Timestamp timestamp = rs.getTimestamp(5);
            contribution.setDay(timestamp.getDay());
            contribution.setMonth(timestamp.getMonth());
            contribution.setYear(timestamp.getYear());
            contribution.setValue(rs.getInt(6));
            contribution.setStatus(Contribution.Status.valueOf(rs.getString(7)));
            if (expectApprovalDate) {
                timestamp = rs.getTimestamp(8);
                contribution.setApproveDay(timestamp.getDay());
                contribution.setApproveMonth(timestamp.getMonth());
                contribution.setApproveYear(timestamp.getYear());
            }
            contributions.add(contribution);
        }
        return contributions;
    }
}
