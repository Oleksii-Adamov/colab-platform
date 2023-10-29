package com.example.colabplatform.services;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Skill;

import java.sql.SQLException;
import java.util.List;

public class SkillService {
    public List<Skill> getAll() throws SQLException {
        return DAOFactory.getInstance().getSkillDAO().getSkills();
    }
}
