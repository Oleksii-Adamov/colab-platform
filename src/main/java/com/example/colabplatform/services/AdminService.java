package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.infoClasses.ProjectsStats;
import com.example.colabplatform.infoClasses.UsersContributionsStats;

import java.sql.SQLException;

public class AdminService {

    public UsersContributionsStats getNewUsersStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        return DAOFactory.getInstance().getNewUsersStatsByMonthDAO().getStatsInRange(beginningMonth, beginningYear, endMonth, endYear);
    }

    public ProjectsStats getProjectsStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        return DAOFactory.getInstance().getProjectsStatsByMonthDAO().getStatsInRange(beginningMonth, beginningYear, endMonth, endYear);
    }

    public UsersContributionsStats getNewCollaboratorsStatsInRange(Integer beginningMonth, Integer beginningYear, Integer endMonth, Integer endYear) throws SQLException {
        return DAOFactory.getInstance().getNewCollaboratorsStatsByMonthDAO().getStatsInRange(beginningMonth, beginningYear, endMonth, endYear);
    }

}
