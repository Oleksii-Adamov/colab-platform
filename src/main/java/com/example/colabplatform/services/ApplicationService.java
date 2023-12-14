package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.infoClasses.ApplicationInfo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService {
    public Integer create(Integer userId, Integer projectId) throws SQLException {
        return DAOFactory.getInstance().getApplicationDAO().create(new Application(userId, projectId, Application.Status.PENDING));
    }

    public void approve(Integer applicationId, Integer userId, Integer projectId) throws SQLException {
        DAOFactory.getInstance().getApplicationDAO().approve(applicationId, userId, projectId);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Integer month = timestamp.toLocalDateTime().getMonthValue();
        Integer year = timestamp.toLocalDateTime().getYear();
        DAOFactory.getInstance().getProjectNewUsersStatsByMonthDAO().countInUser(projectId, month, year);
        DAOFactory.getInstance().getNewCollaboratorsStatsByMonthDAO().countInUser(month, year);
    }

    public void reject(Integer applicationId) throws SQLException {
        DAOFactory.getInstance().getApplicationDAO().reject(applicationId);
    }

    public List<ApplicationInfo> getProjectPendingApplications(Integer projectId) throws SQLException {
        List<Application> applications = DAOFactory.getInstance().getApplicationDAO().getProjectPendingApplications(projectId);
        List<ApplicationInfo> applicationInfos = new ArrayList<>();
        for (Application application : applications) {
            String userFullName = DAOFactory.getInstance().getUserDAO().getFullNameById(application.getUserId());
            applicationInfos.add(new ApplicationInfo(userFullName, application));
        }
        return applicationInfos;
    }
}
