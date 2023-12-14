package com.example.colabplatform.dao;

import com.example.colabplatform.infoClasses.UsersContributionsStats;

import java.sql.SQLException;

public interface ProjectNewUsersStatsByMonthDAO {
    void countInUser(Integer projectId, Integer month, Integer year) throws SQLException;
    void countInContribution(Integer projectId, Integer month, Integer year, Integer value) throws SQLException;
    UsersContributionsStats getStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException;
}
