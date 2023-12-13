package com.example.colabplatform.enitities;

import com.example.colabplatform.exceptions.ProjectException;

import java.util.ArrayList;
import java.util.List;

public class Project implements Entity {

    private Integer id;
    private String name;
    private Integer creatorUserID;

    private String description;

    private List<Tag> tags = new ArrayList<>();

    private List<Skill> skills = new ArrayList<>();

    private Float rating;

    private Integer numberOfRatings;

    private Boolean isFinished;

    public Project() {
    }

    public Project(String name, Integer creatorUserID) {
        this.name = name;
        this.creatorUserID = creatorUserID;
    }

    public Project(String name, Integer creatorUserID, String description) {
        this.name = name;
        this.creatorUserID = creatorUserID;
        this.description = description;
    }

    public Project(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public void setFinished(Integer finished) {
        if (finished == 1) {
            this.isFinished = true;
        }
        else if (finished == 0) {
            this.isFinished = false;
        }
        else {
            throw new ProjectException("Invalid argument finished, must be 0 or 1");
        }
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreatorUserID() {
        return creatorUserID;
    }

    public void setCreatorUserID(Integer creatorUserID) {
        this.creatorUserID = creatorUserID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
