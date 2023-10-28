package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.UserDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    @Override
    public void create(User user) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO USERS (UserFullName, KeycloakUserID) VALUES(?, ?)");
        preparedStatement.setString(1, user.getFullName());
        preparedStatement.setString(2, user.getKeycloakId());
        preparedStatement.execute();
    }

    @Override
    public User getByKeycloakId(String keycloakUserId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("SELECT u.UserID, u.KeycloakUserID, u.UserFullName FROM USERS u " +
                        "WHERE u.KeycloakUserID = ?");
        preparedStatement.setString(1, keycloakUserId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setKeycloakId(rs.getString(2));
            user.setFullName(rs.getString(3));
            return user;
        }
        else {
            return null;
        }
    }

}
