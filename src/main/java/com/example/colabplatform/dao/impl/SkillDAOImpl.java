package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.SkillDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDAOImpl implements SkillDAO {

    @Override
    public List<Skill> getSkills() throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT sk.SKILLID, sk.SKILLNAME FROM SKILLS sk");
        ResultSet rs = preparedStatement.executeQuery();
        return skillsFromResultSet(rs);
    }
    private List<Skill> skillsFromResultSet(ResultSet rs) throws SQLException {
        List<Skill> skills = new ArrayList<>();
        while (rs.next()) {
            Skill skill = new Skill();
            skill.setId(rs.getInt(1));
            skill.setName(rs.getString(2));
            skills.add(skill);
        }
        return skills;
    }
}
