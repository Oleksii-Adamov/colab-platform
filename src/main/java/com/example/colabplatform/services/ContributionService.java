package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.dao.impl.ProjectNewUsersStatsByMonthDAOImpl;
import com.example.colabplatform.enitities.*;
import com.example.colabplatform.exceptions.ContributionServiceException;
import com.example.colabplatform.infoClasses.ApplicationInfo;
import com.example.colabplatform.infoClasses.ContributionInfo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContributionService {

    protected static final Logger logger = LogManager.getLogger(ContributionService.class);
    public Integer create(Integer userId, Integer projectId, String description, Integer value) throws SQLException, ContributionServiceException {
        Contribution.Status status = Contribution.Status.PENDING;
        Collaborator collaborator = DAOFactory.getInstance().getCollaboratorDAO().getByUserAndProjectId(userId, projectId);
        if (collaborator == null) {
            throw new ContributionServiceException("You are not collaborator");
        }
        Contribution contribution = new Contribution(userId, projectId, description, value, Contribution.Status.PENDING);
        if (collaborator.getAdmin()) {
            contribution.setStatus(Contribution.Status.APPROVED);
            countInContribution(contribution);
        }
        return DAOFactory.getInstance().getContributionDAO().create(new Contribution(userId, projectId, description, value, status));
    }

    public void approve(Integer contributionId) throws SQLException {
        logger.info("approve");
        DAOFactory.getInstance().getContributionDAO().approve(contributionId);
        logger.info("approved");
        countInContribution(DAOFactory.getInstance().getContributionDAO().getById(contributionId));
    }

    public void reject(Integer contributionId) throws SQLException {
        DAOFactory.getInstance().getContributionDAO().reject(contributionId);
    }

    public List<ContributionInfo> getProjectPendingContributions(Integer projectId) throws SQLException {
        List<Contribution> contributions = DAOFactory.getInstance().getContributionDAO().getProjectPendingContributions(projectId);
        return attachUserInfoToContributions(contributions);
    }

    public List<ContributionInfo> getProjectApprovedContributions(Integer projectId) throws SQLException {
        List<Contribution> contributions = DAOFactory.getInstance().getContributionDAO().getProjectApprovedContributions(projectId);
        return attachUserInfoToContributions(contributions);
    }

    private List<ContributionInfo> attachUserInfoToContributions(List<Contribution> contributions) throws SQLException {
        List<ContributionInfo> contributionInfos = new ArrayList<>();
        for (Contribution contribution : contributions) {
            String userFullName = DAOFactory.getInstance().getUserDAO().getFullNameById(contribution.getUserId());
            contributionInfos.add(new ContributionInfo(userFullName, contribution));
        }
        return contributionInfos;
    }

    private void countInContribution(Contribution contribution) throws SQLException {
        DAOFactory.getInstance().getCollaboratorDAO().countInContribution(
                DAOFactory.getInstance().getCollaboratorDAO().getByUserAndProjectId(contribution.getUserId(), contribution.getProjectId()).getId(), contribution.getValue());

        Integer month = contribution.getMonth();
        Integer year = contribution.getYear();
        logger.info("Month and year");
        logger.info(month);
        logger.info(year);
        if (month == null || year == null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            month = timestamp.toLocalDateTime().getMonthValue();
            year = timestamp.toLocalDateTime().getYear();
        }
        logger.info("Month and year again");
        logger.info(month);
        logger.info(year);
        DAOFactory.getInstance().getProjectContributionsStatsByMonthDAO().countInContribution(contribution.getProjectId(), month, year, contribution.getValue());
        logger.info("usual counted in contrib");

        Collaborator collaborator = DAOFactory.getInstance().getCollaboratorDAO().getByUserAndProjectId(contribution.getUserId(), contribution.getProjectId());
        logger.info(collaborator.getMonthOfJoining());
        logger.info(collaborator.getYearOfJoining());
        if (Objects.equals(collaborator.getMonthOfJoining(), month) && Objects.equals(collaborator.getYearOfJoining(), year)) {
            logger.info("calling getProjectNewUsersStatsByMonthDAO().countInContribution");
            DAOFactory.getInstance().getProjectNewUsersStatsByMonthDAO().countInContribution(contribution.getProjectId(), month, year, contribution.getValue());
            logger.info("counted in getProjectNewUsersStatsByMonthDAO().countInContribution");
            DAOFactory.getInstance().getNewCollaboratorsStatsByMonthDAO().countInContribution(month, year, contribution.getValue());
        }

        User user = DAOFactory.getInstance().getUserDAO().getUserInfo(contribution.getUserId());
        if (Objects.equals(user.getMonthOfJoining(), month) && Objects.equals(user.getYearOfJoining(), year)) {
            logger.info("calling getNewUsersStatsByMonthDAO().countInContribution");
            DAOFactory.getInstance().getNewUsersStatsByMonthDAO().countInContribution(month, year, contribution.getValue());
            logger.info("counted in getNewUsersStatsByMonthDAO().countInContribution");
        }

        DAOFactory.getInstance().getProjectsStatsByMonthDAO().countInContribution(month, year);
    }
}
