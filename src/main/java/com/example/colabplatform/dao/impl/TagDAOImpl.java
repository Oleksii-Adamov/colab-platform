package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.TagDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAOImpl implements TagDAO {

    @Override
    public List<Tag> getTags() throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT tg.TAGID, tg.TAGNAME FROM TAGS tg");
        ResultSet rs = preparedStatement.executeQuery();
        return tagsFromResultSet(rs);
    }
    private List<Tag> tagsFromResultSet(ResultSet rs) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        while (rs.next()) {
            Tag tag = new Tag();
            tag.setId(rs.getInt(1));
            tag.setName(rs.getString(2));
            tags.add(tag);
        }
        return tags;
    }
}
