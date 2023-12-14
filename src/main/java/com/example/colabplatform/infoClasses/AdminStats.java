package com.example.colabplatform.infoClasses;

public class AdminStats {
    UsersContributionsStats newUsersStats;
    ProjectsStats projectsStats;
    UsersContributionsStats newCollaboratorsStats;

    public AdminStats(UsersContributionsStats newUsersStats, ProjectsStats projectsStats, UsersContributionsStats newCollaboratorsStats) {
        this.newUsersStats = newUsersStats;
        this.projectsStats = projectsStats;
        this.newCollaboratorsStats = newCollaboratorsStats;
    }

    public UsersContributionsStats getNewUsersStats() {
        return newUsersStats;
    }

    public void setNewUsersStats(UsersContributionsStats newUsersStats) {
        this.newUsersStats = newUsersStats;
    }

    public ProjectsStats getProjectsStats() {
        return projectsStats;
    }

    public void setProjectsStats(ProjectsStats projectsStats) {
        this.projectsStats = projectsStats;
    }

    public UsersContributionsStats getNewCollaboratorsStats() {
        return newCollaboratorsStats;
    }

    public void setNewCollaboratorsStats(UsersContributionsStats newCollaboratorsStats) {
        this.newCollaboratorsStats = newCollaboratorsStats;
    }
}
