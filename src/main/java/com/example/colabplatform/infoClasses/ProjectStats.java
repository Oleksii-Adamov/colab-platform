package com.example.colabplatform.infoClasses;

public class ProjectStats {
    private ContributionsStats contributionsStats;
    // UsersContributionsStats newUsersStats;
    // UserProjectStatsByMonth

    public ProjectStats(ContributionsStats contributionsStats) {
        this.contributionsStats = contributionsStats;
    }

    public ContributionsStats getContributionsStats() {
        return contributionsStats;
    }

    public void setContributionsStats(ContributionsStats contributionsStats) {
        this.contributionsStats = contributionsStats;
    }
}
