package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.User;

import java.sql.SQLException;

public class UserService {
    public Integer registerIfNot(String keycloakId, String fullName) throws SQLException {
        User user = DAOFactory.getInstance().getUserDAO().getByKeycloakId(keycloakId);
        if(user == null) {
            return DAOFactory.getInstance().getUserDAO().create(new User(keycloakId, fullName));
        }
        else {
            return user.getId();
        }
    }
}
