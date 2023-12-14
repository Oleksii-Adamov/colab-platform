package com.example.colabplatform.infoClasses;

public class UsersContributionsStats {
    private Integer numberOfContributions;
    private Integer totalValueOfContributions;
    private Integer numberOfNewUsers;

    public UsersContributionsStats(Integer numberOfContributions, Integer totalValueOfContributions, Integer numberOfNewUsers) {
        this.numberOfContributions = numberOfContributions;
        this.totalValueOfContributions = totalValueOfContributions;
        this.numberOfNewUsers = numberOfNewUsers;
    }
}
