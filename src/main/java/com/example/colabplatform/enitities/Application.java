package com.example.colabplatform.enitities;

import java.time.LocalDate;

public class Application implements Entity {
    private Integer id;
    private Integer userId;
    private Integer projectId;

    private Integer day;

    private Integer month;

    private Integer year;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.toString();
    }
}
