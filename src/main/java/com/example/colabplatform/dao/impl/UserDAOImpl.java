package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.UserDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.User;
import com.example.colabplatform.exceptions.UserDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImpl implements UserDAO {
    @Override
    public Integer create(User user) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO USERS (UserFullName, KeycloakUserID) VALUES(?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getFullName());
        preparedStatement.setString(2, user.getKeycloakId());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        else {
            throw new UserDAOException("Didn't get created user id");
        }
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
