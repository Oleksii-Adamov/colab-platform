package com.example.colabplatform.enitities;

import com.example.colabplatform.exceptions.CollaboratorException;

import java.time.LocalDate;

public class Collaborator implements Entity {
    private Integer id;
    private Integer userId;
    private Integer projectId;

    private Boolean isAdmin;

    private Integer dayOfJoining;

    private Integer monthOfJoining;

    private Integer yearOfJoining;
    private Integer rating;

    public Collaborator() {
    }

    public Collaborator(Integer userId, Integer projectId) {
        this.userId = userId;
        this.projectId = projectId;
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

    public Integer getDayOfJoining() {
        return dayOfJoining;
    }

    public void setDayOfJoining(Integer dayOfJoining) {
        this.dayOfJoining = dayOfJoining;
    }

    public Integer getMonthOfJoining() {
        return monthOfJoining;
    }

    public void setMonthOfJoining(Integer monthOfJoining) {
        this.monthOfJoining = monthOfJoining;
    }

    public Integer getYearOfJoining() {
        return yearOfJoining;
    }

    public void setYearOfJoining(Integer yearOfJoining) {
        this.yearOfJoining = yearOfJoining;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    public void setAdmin(Integer admin) {
        if (admin == 1) {
            this.isAdmin = true;
        }
        else if (admin == 0) {
            this.isAdmin = false;
        }
        else {
            throw new CollaboratorException("Invalid argument admin, must be 0 or 1");
        }
    }
}
