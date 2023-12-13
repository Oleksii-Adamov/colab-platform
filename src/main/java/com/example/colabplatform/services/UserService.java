package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.User;

import java.sql.SQLException;
import java.util.List;

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

    public User getUserInfo(Integer userId) throws SQLException {
        return DAOFactory.getInstance().getUserDAO().getUserInfo(userId);
    }

    public void update(Integer userId, String newBio, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException {
        User newUserInfo = new User();
        newUserInfo.setId(userId);
        newUserInfo.setBio(newBio);
        DAOFactory.getInstance().getUserDAO().update(newUserInfo, newTagsIds, newSkillsIds);
    }
}
