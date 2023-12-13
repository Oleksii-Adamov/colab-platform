package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.enitities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    Integer create(User user) throws SQLException;

    User getByKeycloakId(String keycloakUserId) throws SQLException;

    String getFullNameById(Integer userId)  throws SQLException;

    User getUserInfo(Integer userId) throws SQLException;

    void update(User newUserInfo, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException;
}
