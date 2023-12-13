package com.example.colabplatform.dao;

import com.example.colabplatform.infoClasses.ContributionsStats;

import java.sql.SQLException;

public interface ProjectContributionsStatsByMonthDAO {
    void countInContribution(Integer projectId, Integer month, Integer year, Integer value) throws SQLException;
    ContributionsStats getStatsInRange(Integer projectId, Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException;
}
