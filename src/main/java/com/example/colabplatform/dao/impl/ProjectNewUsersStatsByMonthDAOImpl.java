package com.example.colabplatform.dao.impl;

import com.example.colabplatform.controllers.AbstractController;
import com.example.colabplatform.dao.ProjectNewUsersStatsByMonthDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.infoClasses.ContributionsStats;
import com.example.colabplatform.infoClasses.UsersContributionsStats;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectNewUsersStatsByMonthDAOImpl implements ProjectNewUsersStatsByMonthDAO {

    protected static final Logger logger = LogManager.getLogger(ProjectNewUsersStatsByMonthDAOImpl.class);
    @Override
    public void countInUser(Integer projectId, Integer month, Integer year) throws SQLException {
        // check if already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT NUMBEROFNEWUSERS" +
                        " FROM PROJECTNEWUSERSSTATSBYMONTH" +
                        " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, month);
        preparedStatement.setInt(3, year);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevNumberOfNewUsers = rs.getInt(1);
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE PROJECTNEWUSERSSTATSBYMONTH" +
                            " SET NUMBEROFNEWUSERS = ?" +
                            " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumberOfNewUsers + 1);
            preparedStatement.setInt(2, projectId);
            preparedStatement.setInt(3, month);
            preparedStatement.setInt(4, year);
            int rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("PROJECTNEWUSERSSTATSBYMONTH update failed");
            }
        }
        else {
            // create new entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("INSERT INTO PROJECTNEWUSERSSTATSBYMONTH (ProjectID, MONTH, YEAR, NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS, NUMBEROFNEWUSERS)" +
                            " VALUES(?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 1);
            preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
        }
    }

    @Override
    public void countInContribution(Integer projectId, Integer month, Integer year, Integer value) throws SQLException {
        logger.info("countInContribution");
        // check if already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS" +
                        " FROM PROJECTNEWUSERSSTATSBYMONTH" +
                        " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, month);
        preparedStatement.setInt(3, year);
        ResultSet rs = preparedStatement.executeQuery();
        logger.info("executeQuery 1 SELECT");
        if (rs.next()) {
            Integer prevNumberOfContributions = rs.getInt(1);
            Integer prevTotalValueOfContributions = rs.getInt(2);
            logger.info("executeQuery 1 read values " + prevNumberOfContributions.toString() + " " + prevTotalValueOfContributions.toString());
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE PROJECTNEWUSERSSTATSBYMONTH" +
                            " SET NUMBEROFCONTRIBUTIONS = ?, TOTALVALUEOFCONTRIBUTIONS = ?" +
                            " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumberOfContributions + 1);
            preparedStatement.setInt(2, prevTotalValueOfContributions + value);
            preparedStatement.setInt(3, projectId);
            preparedStatement.setInt(4, month);
            preparedStatement.setInt(5, year);
            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("executeUpdate 2 UPDATE");
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("PROJECTNEWUSERSSTATSBYMONTH update failed");
            }
        }
        else {
            // should first count in user
//            countInUser(projectId, month, year);
//            countInContribution(projectId, month, year, value);
            ConnectionFactory.instance().releaseConnection();
            throw new ProjectDAOException("PROJECTNEWUSERSSTATSBYMONTH creation by contribution is fail");
        }
    }

    @Override
    public UsersContributionsStats getStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        logger.info("getStatsInRange");
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT " +
                        "SUM(NUMBEROFCONTRIBUTIONS) AS TotalContributions, " +
                        "SUM(TOTALVALUEOFCONTRIBUTIONS) AS TotalValue, " +
                        "SUM(NUMBEROFNEWUSERS) AS TotalUsers " +
                        "FROM " +
                        "PROJECTNEWUSERSSTATSBYMONTH " +
                        "WHERE " +
                        "projectId = ? " +
                        "AND ((Year > ? AND Year < ?) " +
                        "OR (Year = ? AND Year = ? AND Month >= ? AND Month <= ?) " +
                        "OR (Year = ? AND Year < ? AND Month >= ?) " +
                        "OR (Year > ? AND Year = ? AND Month <= ?))");
        preparedStatement.setInt(1, projectId);

        preparedStatement.setInt(2, beginningYear);
        preparedStatement.setInt(3, endYear);

        preparedStatement.setInt(4, beginningYear);
        preparedStatement.setInt(5, endYear);
        preparedStatement.setInt(6, beginningMonth);
        preparedStatement.setInt(7, endMonth);

        preparedStatement.setInt(8, beginningYear);
        preparedStatement.setInt(9, endYear);
        preparedStatement.setInt(10, beginningMonth);

        preparedStatement.setInt(11, beginningYear);
        preparedStatement.setInt(12, endYear);
        preparedStatement.setInt(13, endMonth);

        ResultSet rs = preparedStatement.executeQuery();
        logger.info("executeQuery getStatsInRange");
        ConnectionFactory.instance().releaseConnection();
        if (rs.next()) {
            Integer totalContributions = rs.getInt(1);
            Integer totalValue = rs.getInt(2);
            Integer totalUsers = rs.getInt(3);
            logger.info("readValues getStatsInRange " + totalContributions.toString() + " " + totalValue.toString() + " " +
                    totalUsers.toString());
            return new UsersContributionsStats(totalContributions, totalValue, totalUsers);
        }
        else {
            return null;
        }
    }
}
