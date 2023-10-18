package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {
    List<Project> getProjects() throws SQLException;
}
