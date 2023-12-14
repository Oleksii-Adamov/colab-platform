package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.ProjectsStatsByMonthDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.exceptions.UserDAOException;
import com.example.colabplatform.infoClasses.ProjectsStats;
import com.example.colabplatform.infoClasses.UsersContributionsStats;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ProjectsStatsByMonthDAOImpl implements ProjectsStatsByMonthDAO {

    protected static final Logger logger = LogManager.getLogger(ProjectsStatsByMonthDAOImpl.class);
    @Override
    public void countInCreated(Integer month, Integer year) throws SQLException {
        countInColumn(month, year, "NUMBEROFCREATEDPROJECTS");
        // check if already exists
//        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
//                prepareStatement("SELECT NUMBEROFCREATEDPROJECTS" +
//                        " FROM PROJECTSSTATSBYMONTH" +
//                        " WHERE MONTH = ? AND YEAR = ?");
//        preparedStatement.setInt(1, month);
//        preparedStatement.setInt(2, year);
//        ResultSet rs = preparedStatement.executeQuery();
//        if (rs.next()) {
//            Integer prevNumberOfProjects = rs.getInt(1);
//            // update
//            preparedStatement = ConnectionFactory.instance().getConnection().
//                    prepareStatement("UPDATE PROJECTSSTATSBYMONTH" +
//                            " SET NUMBEROFCREATEDPROJECTS = ?" +
//                            " WHERE MONTH = ? AND YEAR = ?");
//            preparedStatement.setInt(1, prevNumberOfProjects + 1);
//            preparedStatement.setInt(2, month);
//            preparedStatement.setInt(3, year);
//            int rowsAffected = preparedStatement.executeUpdate();
//            ConnectionFactory.instance().releaseConnection();
//            if (rowsAffected < 1) {
//                throw new ProjectDAOException("NUMBEROFCREATEDPROJECTS update failed");
//            }
//        }
//        else {
//            // create new entry
//            preparedStatement = ConnectionFactory.instance().getConnection().
//                    prepareStatement("INSERT INTO PROJECTSSTATSBYMONTH (MONTH, YEAR, NUMBEROFFINISHEDPROJECTS, NUMBEROFCREATEDPROJECTS, NUMBEROFCONTRIBUTIONS)" +
//                            " VALUES(?, ?, ?, ?, ?)");
//            preparedStatement.setInt(1, month);
//            preparedStatement.setInt(2, year);
//            preparedStatement.setInt(3, 0);
//            preparedStatement.setInt(4, 1);
//            preparedStatement.setInt(5, 0);
//            preparedStatement.executeUpdate();
//            ConnectionFactory.instance().releaseConnection();
//        }
    }

    @Override
    public void countInFinished(Integer month, Integer year) throws SQLException {
        countInColumn(month, year, "NUMBEROFFINISHEDPROJECTS");
    }

    @Override
    public void countInContribution(Integer month, Integer year) throws SQLException {
        countInColumn(month, year, "NUMBEROFCONTRIBUTIONS");
    }

    private void countInColumn(Integer month, Integer year, String columnName) throws SQLException {
        // check if already exists
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT " + columnName +
                        " FROM PROJECTSSTATSBYMONTH" +
                        " WHERE MONTH = ? AND YEAR = ?");
        logger.info("SELECT " + columnName +
                " FROM PROJECTSSTATSBYMONTH" +
                " WHERE MONTH = ? AND YEAR = ?");
        preparedStatement.setInt(1, month);
        preparedStatement.setInt(2, year);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Integer prevNumber = rs.getInt(1);
            // update
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("UPDATE PROJECTSSTATSBYMONTH" +
                            " SET " + columnName + " = ?" +
                            " WHERE MONTH = ? AND YEAR = ?");
            logger.info("UPDATE PROJECTSSTATSBYMONTH" +
                    " SET " + columnName + " = ?" +
                    " WHERE MONTH = ? AND YEAR = ?");
            preparedStatement.setInt(1, prevNumber + 1);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            int rowsAffected = preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
            if (rowsAffected < 1) {
                throw new ProjectDAOException("NUMBEROFCREATEDPROJECTS update failed");
            }
        }
        else {
            // create new entry
            preparedStatement = ConnectionFactory.instance().getConnection().
                    prepareStatement("INSERT INTO PROJECTSSTATSBYMONTH (MONTH, YEAR, NUMBEROFFINISHEDPROJECTS, NUMBEROFCREATEDPROJECTS, NUMBEROFCONTRIBUTIONS)" +
                            " VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, Objects.equals(columnName, "NUMBEROFFINISHEDPROJECTS") ? 1 : 0);
            preparedStatement.setInt(4, Objects.equals(columnName, "NUMBEROFCREATEDPROJECTS") ? 1 : 0);
            preparedStatement.setInt(5, Objects.equals(columnName, "NUMBEROFCONTRIBUTIONS") ? 1 : 0);
            logger.info("CreateNewEntry");
            logger.info(Objects.equals(columnName, "NUMBEROFFINISHEDPROJECTS") ? 1 : 0);
            logger.info(Objects.equals(columnName, "NUMBEROFCREATEDPROJECTS") ? 1 : 0);
            logger.info(Objects.equals(columnName, "NUMBEROFCONTRIBUTIONS") ? 1 : 0);
            preparedStatement.executeUpdate();
            ConnectionFactory.instance().releaseConnection();
        }
    }

    @Override
    public ProjectsStats getStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().
                prepareStatement("SELECT " +
                        "SUM(NUMBEROFFINISHEDPROJECTS) AS TotalFinished, " +
                        "SUM(NUMBEROFCREATEDPROJECTS) AS TotalCreated, " +
                        "SUM(NUMBEROFCONTRIBUTIONS) AS TotalContributions " +
                        "FROM " +
                        "PROJECTSSTATSBYMONTH " +
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
            Integer totalFinished = rs.getInt(1);
            Integer totalCreated = rs.getInt(2);
            Integer totalContributions = rs.getInt(3);
            return new ProjectsStats(totalFinished, totalCreated, totalContributions);
        }
        else {
            return null;
        }
    }
}
