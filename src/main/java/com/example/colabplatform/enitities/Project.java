package com.example.colabplatform.enitities;

import java.util.ArrayList;
import java.util.List;

public class Project implements Entity {

    private Integer id;
    private String name;
    private Integer creatorUserID;

    private String description;

    private List<Tag> tags = new ArrayList<>();

    private List<Skill> skills = new ArrayList<>();

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
