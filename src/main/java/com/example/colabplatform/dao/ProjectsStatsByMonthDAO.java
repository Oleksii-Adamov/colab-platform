package com.example.colabplatform.dao;

import com.example.colabplatform.infoClasses.ProjectsStats;

import java.sql.SQLException;

public interface ProjectsStatsByMonthDAO {
    void countInCreated(Integer month, Integer year) throws SQLException;
    void countInFinished(Integer month, Integer year) throws SQLException;
    void countInContribution(Integer month, Integer year) throws SQLException;
    ProjectsStats getStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException;
}
