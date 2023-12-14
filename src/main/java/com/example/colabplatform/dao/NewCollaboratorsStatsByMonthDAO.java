package com.example.colabplatform.dao;

import com.example.colabplatform.infoClasses.UsersContributionsStats;

import java.sql.SQLException;

public interface NewCollaboratorsStatsByMonthDAO {
    void countInUser(Integer month, Integer year) throws SQLException;
    void countInContribution(Integer month, Integer year, Integer value) throws SQLException;
    UsersContributionsStats getStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException;
}
