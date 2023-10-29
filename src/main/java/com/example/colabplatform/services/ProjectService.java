package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Project;

import java.sql.SQLException;

public class ProjectService {
    public Integer create(String name, Integer creatorUserId) throws SQLException {
        return DAOFactory.getInstance().getProjectDAO().create(new Project(name, creatorUserId));
    }

}
