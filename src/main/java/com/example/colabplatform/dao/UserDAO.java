package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.User;

import java.sql.SQLException;

public interface UserDAO {
    Integer create(User user) throws SQLException;

    User getByKeycloakId(String keycloakUserId) throws SQLException;
}
