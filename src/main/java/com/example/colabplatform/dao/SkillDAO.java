package com.example.colabplatform.dao;

import com.example.colabplatform.enitities.Skill;

import java.sql.SQLException;
import java.util.List;

public interface SkillDAO {
    List<Skill> getSkills() throws SQLException;
}
