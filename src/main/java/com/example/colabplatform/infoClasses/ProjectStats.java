package com.example.colabplatform.infoClasses;

public class ProjectStats {
    private ContributionsStats contributionsStats;
    private UsersContributionsStats newUsersStats;

    public ProjectStats(ContributionsStats contributionsStats, UsersContributionsStats newUsersStats) {
        this.contributionsStats = contributionsStats;
        this.newUsersStats = newUsersStats;
    }

    public ContributionsStats getContributionsStats() {
        return contributionsStats;
    }

    public void setContributionsStats(ContributionsStats contributionsStats) {
        this.contributionsStats = contributionsStats;
    }

    public UsersContributionsStats getNewUsersStats() {
        return newUsersStats;
    }

    public void setNewUsersStats(UsersContributionsStats newUsersStats) {
        this.newUsersStats = newUsersStats;
    }
}
