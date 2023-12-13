package com.example.colabplatform.enitities;

public class User implements Entity {

    private Integer id;
    private String keycloakId;
    private String fullName;

    private String bio;

    public User() {
    }

    public User(String keycloakId, String fullName) {
        this.keycloakId = keycloakId;
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
