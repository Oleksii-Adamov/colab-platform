package com.example.colabplatform.dao;

import com.example.colabplatform.dao.impl.*;

public class DAOFactory {
    private static final DAOFactory daoFactory = new DAOFactory();

    private DAOFactory(){}

    private final ProjectDAO projectDAO = new ProjectDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    private final TagDAO tagDAO = new TagDAOImpl();
    private final SkillDAO skillDAO = new SkillDAOImpl();

    private final ApplicationDAO applicationDAO = new ApplicationDAOImpl();

    private final CollaboratorDAO collaboratorDAO = new CollaboratorDAOImpl();

    private final ContributionDAO contributionDAO = new ContributionDAOImpl();

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

    public ApplicationDAO getApplicationDAO() {
        return applicationDAO;
    }

    public CollaboratorDAO getCollaboratorDAO() {
        return collaboratorDAO;
    }

    public ContributionDAO getContributionDAO() {
        return contributionDAO;
    }
}
