package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Tag;

import java.sql.SQLException;
import java.util.List;

public class TagService {
    public List<Tag> getAll() throws SQLException {
        return DAOFactory.getInstance().getTagDAO().getTags();
    }
}
