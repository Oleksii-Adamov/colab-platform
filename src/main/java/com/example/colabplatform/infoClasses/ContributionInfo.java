package com.example.colabplatform.infoClasses;

import com.example.colabplatform.enitities.Contribution;

public class ContributionInfo {
    private String userFullName;
    private Contribution contribution;

    public ContributionInfo(String userFullName, Contribution contribution) {
        this.userFullName = userFullName;
        this.contribution = contribution;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }
}
