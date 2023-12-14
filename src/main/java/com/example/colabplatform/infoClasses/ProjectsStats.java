package com.example.colabplatform.infoClasses;

public class ProjectsStats {
    private Integer numberOfFinishedProjects;
    private Integer numberOfCreatedProjects;
    private Integer numberOfContributions;

    public ProjectsStats(Integer numberOfFinishedProjects, Integer numberOfCreatedProjects, Integer numberOfContributions) {
        this.numberOfFinishedProjects = numberOfFinishedProjects;
        this.numberOfCreatedProjects = numberOfCreatedProjects;
        this.numberOfContributions = numberOfContributions;
    }

    public Integer getNumberOfFinishedProjects() {
        return numberOfFinishedProjects;
    }

    public void setNumberOfFinishedProjects(Integer numberOfFinishedProjects) {
        this.numberOfFinishedProjects = numberOfFinishedProjects;
    }

    public Integer getNumberOfCreatedProjects() {
        return numberOfCreatedProjects;
    }

    public void setNumberOfCreatedProjects(Integer numberOfCreatedProjects) {
        this.numberOfCreatedProjects = numberOfCreatedProjects;
    }

    public Integer getNumberOfContributions() {
        return numberOfContributions;
    }

    public void setNumberOfContributions(Integer numberOfContributions) {
        this.numberOfContributions = numberOfContributions;
    }
}
