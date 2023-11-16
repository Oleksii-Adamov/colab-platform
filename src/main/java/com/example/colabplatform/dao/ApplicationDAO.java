package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Application;

import java.sql.SQLException;
import java.util.List;

public interface ApplicationDAO {
    Integer create(Application application) throws SQLException;

    void approve(Integer applicationId, Integer userId, Integer projectId) throws SQLException;

    void reject(Integer applicationId) throws SQLException;
    List<Application> getProjectPendingApplications(Integer projectId) throws SQLException;
}
