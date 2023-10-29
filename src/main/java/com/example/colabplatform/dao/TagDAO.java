package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Tag;

import java.sql.SQLException;
import java.util.List;

public interface TagDAO {
    List<Tag> getTags() throws SQLException;
}
