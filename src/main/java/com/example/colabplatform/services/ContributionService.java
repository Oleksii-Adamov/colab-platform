package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Contribution;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.ContributionServiceException;
import com.example.colabplatform.infoClasses.ApplicationInfo;
import com.example.colabplatform.infoClasses.ContributionInfo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ContributionService {
    public Integer create(Integer userId, Integer projectId, String description, Integer value) throws SQLException, ContributionServiceException {
        Contribution.Status status = Contribution.Status.PENDING;
        Collaborator collaborator = DAOFactory.getInstance().getCollaboratorDAO().getByUserAndProjectId(userId, projectId);
        if (collaborator == null) {
            throw new ContributionServiceException("You are not collaborator");
        }
        if (collaborator.getAdmin()) {
            status = Contribution.Status.APPROVED;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            DAOFactory.getInstance().getProjectContributionsStatsByMonthDAO().countInContribution(projectId, timestamp.toLocalDateTime().getMonthValue(), timestamp.toLocalDateTime().getYear(), value);
        }
        return DAOFactory.getInstance().getContributionDAO().create(new Contribution(userId, projectId, description, value, status));
    }

    public void approve(Integer contributionId) throws SQLException {
        DAOFactory.getInstance().getContributionDAO().approve(contributionId);
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
}
