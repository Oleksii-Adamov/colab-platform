package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Contribution;

import java.sql.SQLException;
import java.util.List;

public interface ContributionDAO {
    Integer create(Contribution contribution) throws SQLException;

    void approve(Integer contributionId) throws SQLException;

    void reject(Integer contributionId) throws SQLException;
    List<Contribution> getProjectPendingContributions(Integer projectId) throws SQLException;
    List<Contribution> getProjectApprovedContributions(Integer projectId) throws SQLException;
    Contribution getById(Integer contributionId) throws SQLException;
}