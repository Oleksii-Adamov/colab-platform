package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectContributionsStatsByMonthDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.infoClasses.ContributionsStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectContributionsStatsByMonthDAOImpl implements ProjectContributionsStatsByMonthDAO {

    @Override
    public void countInContribution(Integer projectId, Integer month, Integer year, Integer value) throws SQLException {
        // check if rating of this user already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS" +
                        " FROM PROJECTCONTRIBUTIONSSTATSBYMONTH" +
                        " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, month);
        preparedStatement.setInt(3, year);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevNumberOfContributions = rs.getInt(1);
            Integer prevTotalValueOfContributions = rs.getInt(2);
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE PROJECTCONTRIBUTIONSSTATSBYMONTH" +
                            " SET NUMBEROFCONTRIBUTIONS = ?, TOTALVALUEOFCONTRIBUTIONS = ?" +
                            " WHERE PROJECTID = ? AND MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumberOfContributions + 1);
            preparedStatement.setInt(2, prevTotalValueOfContributions + value);
            preparedStatement.setInt(3, projectId);
            preparedStatement.setInt(4, month);
            preparedStatement.setInt(5, year);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("PROJECTCONTRIBUTIONSSTATSBYMONTH update failed");
            }
        }
        else {
            // create new entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("INSERT INTO PROJECTCONTRIBUTIONSSTATSBYMONTH (ProjectID, MONTH, YEAR, NUMBEROFCONTRIBUTIONS, TOTALVALUEOFCONTRIBUTIONS)" +
                            " VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, value);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public ContributionsStats getStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT " +
                        "SUM(NUMBEROFCONTRIBUTIONS) AS TotalContributions, " +
                        "SUM(TOTALVALUEOFCONTRIBUTIONS) AS TotalValue " +
                        "FROM " +
                        "PROJECTCONTRIBUTIONSSTATSBYMONTH " +
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
        if (rs.next()) {
            Integer totalContributions = rs.getInt(1);
            Integer totalValue = rs.getInt(2);
            return new ContributionsStats(totalContributions, totalValue);
        }
        else {
            return null;
        }
    }
}
