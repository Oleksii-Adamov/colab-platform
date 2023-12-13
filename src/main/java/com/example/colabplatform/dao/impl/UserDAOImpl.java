package com.example.colabplatform.dao.impl;

import com.example.colabplatform.dao.UserDAO;
import com.example.colabplatform.database.ConnectionFactory;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.enitities.Skill;
import com.example.colabplatform.enitities.Tag;
import com.example.colabplatform.enitities.User;
import com.example.colabplatform.exceptions.ProjectDAOException;
import com.example.colabplatform.exceptions.UserDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public Integer create(User user) throws SQLException {
        String[] returnCols = { "USERID" };
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO USERS (UserFullName, KeycloakUserID) VALUES(?, ?)",
                        returnCols);
        preparedStatement.setString(1, user.getFullName());
        preparedStatement.setString(2, user.getKeycloakId());
        preparedStatement.execute();
        ConnectionFactory.instance().releaseConnection();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        else {
            throw new UserDAOException("Didn't get created user id");
        }
    }

    @Override
    public User getByKeycloakId(String keycloakUserId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("SELECT u.UserID, u.KeycloakUserID, u.UserFullName FROM USERS u " +
                        "WHERE u.KeycloakUserID = ?");
        preparedStatement.setString(1, keycloakUserId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setKeycloakId(rs.getString(2));
            user.setFullName(rs.getString(3));
            return user;
        }
        else {
            return null;
        }
    }

    @Override
    public String getFullNameById(Integer userId) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("SELECT u.UserFullName FROM USERS u " +
                        "WHERE u.UserID = ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        else {
            return null;
        }
    }

    @Override
    public User getUserInfo(Integer userId) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT U.USERFULLNAME, U.USERBIO, U.RATING, U.NUMBEROFRATINGS" +
                        " FROM Users U" +
                        " WHERE U.USERID = ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            user.setFullName(rs.getString("USERFULLNAME"));
            user.setBio(rs.getString("USERBIO"));
            user.setRating(rs.getFloat("RATING"));
            user.setNumberOfRatings(rs.getInt("NUMBEROFRATINGS"));
        }

        if (user.getFullName() == null) return null;

        preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT T.TAGID, T.TAGNAME" +
                        " FROM Users U" +
                        " INNER JOIN USERTAGSASSIGNMENTS UT on U.USERID = UT.USERID" +
                        " INNER JOIN TAGS T on UT.TAGID = T.TAGID" +
                        " WHERE U.USERID = ?");
        preparedStatement.setInt(1, userId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            user.getTags().add(new Tag(rs.getInt("TAGID"),rs.getString("TAGNAME")));
        }

        preparedStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "SELECT S.SKILLID, S.SKILLNAME" +
                        " FROM Users U" +
                        " INNER JOIN USERSKILLASSIGNMENTS US on U.USERID = US.USERID" +
                        " INNER JOIN SKILLS S on S.SKILLID = US.SKILLID" +
                        " WHERE U.USERID = ?");
        preparedStatement.setInt(1, userId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            user.getSkills().add(new Skill(rs.getInt("SKILLID"), rs.getString("SKILLNAME")));
        }

        ConnectionFactory.instance().releaseConnection();
        return user;
    }

    @Override
    public void update(User newUserInfo, List<Integer> newTagsIds, List<Integer> newSkillsIds) throws SQLException {
        PreparedStatement preparedStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("UPDATE Users U" +
                        " SET U.USERBIO = ?" +
                        " WHERE U.USERID = ?");
        preparedStatement.setString(1, newUserInfo.getBio());
        preparedStatement.setInt(2, newUserInfo.getId());
        Integer rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected < 1) {
            throw new UserDAOException("User update failed");
        }

        PreparedStatement deleteStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "DELETE FROM USERTAGSASSIGNMENTS WHERE USERID = ?");
        deleteStatement.setInt(1, newUserInfo.getId());
        deleteStatement.executeUpdate();
        PreparedStatement insertStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO USERTAGSASSIGNMENTS (USERID, TAGID) VALUES(?, ?)");
        for (int tagId : newTagsIds) {
            insertStatement.setInt(1, newUserInfo.getId());
            insertStatement.setInt(2, tagId);
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();

        deleteStatement = ConnectionFactory.instance().getConnection().prepareStatement(
                "DELETE FROM USERSKILLASSIGNMENTS WHERE USERID = ?");
        deleteStatement.setInt(1, newUserInfo.getId());
        deleteStatement.executeUpdate();
        insertStatement = ConnectionFactory.instance().getConnection()
                .prepareStatement("INSERT INTO USERSKILLASSIGNMENTS (USERID, SKILLID) VALUES(?, ?)");
        for (int skillId : newSkillsIds) {
            insertStatement.setInt(1, newUserInfo.getId());
            insertStatement.setInt(2, skillId);
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
        ConnectionFactory.instance().releaseConnection();
    }
}
