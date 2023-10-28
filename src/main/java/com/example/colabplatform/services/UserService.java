package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.User;

import java.sql.SQLException;

public class UserService {
    public void registerIfNot(String keycloakId, String fullName) throws SQLException {
        if(DAOFactory.getInstance().getUserDAO().getByKeycloakId(keycloakId) == null) {
            DAOFactory.getInstance().getUserDAO().create(new User(keycloakId, fullName));
        }
    }
}
