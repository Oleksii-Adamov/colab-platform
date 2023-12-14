package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.NewCollaboratorsStatsByMonthDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.exceptions.UserDAOException;
import com.example.colabplatform.infoClasses.UsersContributionsStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewCollaboratorsStatsByMonthDAOImpl implements NewCollaboratorsStatsByMonthDAO {
    @Override
    public void countInUser(Integer month, Integer year) throws SQLException {
        // check if already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT NUMBEROFNEWCOLLABORATORS" +
                        " FROM NEWCOLLABORATORSSTATSBYMONTH" +
                        " WHERE MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, month);
        preparedStatement.setInt(2, year);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevNumberOfNewUsers = rs.getInt(1);
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE NEWCOLLABORATORSSTATSBYMONTH" +
                            " SET NUMBEROFNEWCOLLABORATORS = ?" +
                            " WHERE MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumberOfNewUsers + 1);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            int rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new UserDAOException("NEWCOLLABORATORSSTATSBYMONTH update failed");
            }
        }
        else {
            // create new entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("INSERT INTO NEWCOLLABORATORSSTATSBYMONTH (MONTH, YEAR, NUMBEROFNEWCOLLABORATORS, NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS)" +
                            " VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
        }
    }

    @Override
    public void countInContribution(Integer month, Integer year, Integer value) throws SQLException {
        // check if already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS" +
                        " FROM NEWCOLLABORATORSSTATSBYMONTH" +
                        " WHERE MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, month);
        preparedStatement.setInt(2, year);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevNumberOfContributions = rs.getInt(1);
            Integer prevTotalValueOfContributions = rs.getInt(2);
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE NEWCOLLABORATORSSTATSBYMONTH" +
                            " SET NUMBEROFCONTRIBUTIONS = ?, TOTALVALUEOFCONTRIBUTIONS = ?" +
                            " WHERE MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumberOfContributions + 1);
            preparedStatement.setInt(2, prevTotalValueOfContributions + value);
            preparedStatement.setInt(3, month);
            preparedStatement.setInt(4, year);
            int rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new UserDAOException("NEWCOLLABORATORSSTATSBYMONTH update failed");
            }
        }
        else {
            // should first count in user
            ConnectionFactory.instance().releaseConnection();
            throw new UserDAOException("NEWCOLLABORATORSSTATSBYMONTH creation by contribution is fail");
        }
    }

    @Override
    public UsersContributionsStats getStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT " +
                        "SUM(NUMBEROFCONTRIBUTIONS) AS TotalContributions, " +
                        "SUM(TOTALVALUEOFCONTRIBUTIONS) AS TotalValue, " +
                        "SUM(NUMBEROFNEWCOLLABORATORS) AS TotalUsers " +
                        "FROM " +
                        "NEWCOLLABORATORSSTATSBYMONTH " +
                        "WHERE " +
                        "(Year > ? AND Year < ?) " +
                        "OR (Year = ? AND Year = ? AND Month >= ? AND Month <= ?) " +
                        "OR (Year = ? AND Year < ? AND Month >= ?) " +
                        "OR (Year > ? AND Year = ? AND Month <= ?)");

        preparedStatement.setInt(1, beginningYear);
        preparedStatement.setInt(2, endYear);

        preparedStatement.setInt(3, beginningYear);
        preparedStatement.setInt(4, endYear);
        preparedStatement.setInt(5, beginningMonth);
        preparedStatement.setInt(6, endMonth);

        preparedStatement.setInt(7, beginningYear);
        preparedStatement.setInt(8, endYear);
        preparedStatement.setInt(9, beginningMonth);

        preparedStatement.setInt(10, beginningYear);
        preparedStatement.setInt(11, endYear);
        preparedStatement.setInt(12, endMonth);

        ResultSet rs = preparedStatement.executeQuery();
        ConnectionFactory.instance().releaseConnection();
        if (rs.next()) {
            Integer totalContributions = rs.getInt(1);
            Integer totalValue = rs.getInt(2);
            Integer totalUsers = rs.getInt(3);
            return new UsersContributionsStats(totalContributions, totalValue, totalUsers);
        }
        else {
            return null;
        }
    }
}
