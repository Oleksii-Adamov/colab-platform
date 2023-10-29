package com.example.colabplatform.dao;

import com.example.colabplatform.dao.impl.ProjectDAOImpl;
import com.example.colabplatform.dao.impl.SkillDAOImpl;
import com.example.colabplatform.dao.impl.TagDAOImpl;
import com.example.colabplatform.dao.impl.UserDAOImpl;

public class DAOFactory {
    private static final DAOFactory daoFactory = new DAOFactory();

    private DAOFactory(){}

    private final ProjectDAO projectDAO = new ProjectDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    private final TagDAO tagDAO = new TagDAOImpl();
    private final SkillDAO skillDAO = new SkillDAOImpl();

    public static DAOFactory getInstance(){
        return daoFactory;
    }

    public ProjectDAO getProjectDAO(){
        return projectDAO;
    }
    public UserDAO getUserDAO() {
        return userDAO;
    }

    public TagDAO getTagDAO() {
        return tagDAO;
    }

    public SkillDAO getSkillDAO() {
        return skillDAO;
    }
}
