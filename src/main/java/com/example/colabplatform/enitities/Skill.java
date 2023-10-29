package com.example.colabplatform.enitities;

public class Skill implements Entity {
    private Integer id;
    private String name;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
    }

    public Integer getId() {
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
}
