package com.example.colabplatform.enitities;

public class Project implements Entity {

    private Integer id;
    private String name;

    private Integer creatorUserID;


    public Project() {
    }

    public Project(String name, Integer creatorUserID) {
        this.name = name;
        this.creatorUserID = creatorUserID;
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
}
