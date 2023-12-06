package com.example.colabplatform.enitities;

public class Contribution implements Entity {
    private Integer id;
    private Integer userId;
    private Integer projectId;
    private String description;
    private Integer value;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer approveDay;
    private Integer approveMonth;
    private Integer approveYear;
    private String status;
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Contribution() {
    }

    public Contribution(Integer userId, Integer projectId, String description, Integer value, Status status) {
        this.userId = userId;
        this.projectId = projectId;
        this.description = description;
        this.value = value;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    public Integer getApproveDay() {
        return approveDay;
    }

    public void setApproveDay(Integer approveDay) {
        this.approveDay = approveDay;
    }

    public Integer getApproveMonth() {
        return approveMonth;
    }

    public void setApproveMonth(Integer approveMonth) {
        this.approveMonth = approveMonth;
    }

    public Integer getApproveYear() {
        return approveYear;
    }

    public void setApproveYear(Integer approveYear) {
        this.approveYear = approveYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Contribution.Status status) {
        this.status = status.toString();
    }
}
