package com.example.colabplatform.infoClasses;

public class ContributionsStats {
    private Integer numberOfContributions;
    private Integer totalValueOfContributions;

    public ContributionsStats() {
    }

    public ContributionsStats(Integer numberOfContributions, Integer totalValueOfContributions) {
        this.numberOfContributions = numberOfContributions;
        this.totalValueOfContributions = totalValueOfContributions;
    }

    public Integer getNumberOfContributions() {
        return numberOfContributions;
    }

    public void setNumberOfContributions(Integer numberOfContributions) {
        this.numberOfContributions = numberOfContributions;
    }

    public Integer getTotalValueOfContributions() {
        return totalValueOfContributions;
    }

    public void setTotalValueOfContributions(Integer totalValueOfContributions) {
        this.totalValueOfContributions = totalValueOfContributions;
    }
}
