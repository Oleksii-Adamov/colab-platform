package com.example.colabplatform.enitities;

import java.time.LocalDate;

public class Application implements Entity {
    private Integer id;
    private Integer userId;
    private Integer projectId;
    private LocalDate applicationDate;
    private String status;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Application() {
    }

    public Application(Integer userId, Integer projectId, Status status) {
        this.userId = userId;
        this.projectId = projectId;
        this.status = status.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.toString();
    }
}
