package com.example.colabplatform.dao;

import com.example.colabplatform.dao.impl.ProjectDAOImpl;

public class DAOFactory {
    private static final DAOFactory daoFactory = new DAOFactory();

    private DAOFactory(){}

    private final ProjectDAO projectDAO = new ProjectDAOImpl();

    public static DAOFactory getInstance(){
        return daoFactory;
    }

    public ProjectDAO getprojectDAO(){
        return projectDAO;
    }
}
