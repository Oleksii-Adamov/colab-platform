package com.example.colabplatform.controllers;

import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.enitities.User;
import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;
import com.example.colabplatform.services.UserService;
import com.example.colabplatform.validators.UserValidator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController extends AbstractController {
    private static final UserService userService = new UserService();
    private static final UserValidator userValidator = new UserValidator();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            if (requestMapping("/login")) {
                logger.info("login - checking if first time");
                String keycloakUserId = (String) req.getAttribute("userId");
                String fullName = (String) req.getAttribute("fullName");
                try {
                    userValidator.validateKeycloakId(keycloakUserId);
                    userValidator.validateFullName(fullName);
                } catch (UserValidatorException e) {
                    logger.info(e.getMessage());
                    resp.sendError(400, e.getMessage());
                    return;
                }
                try {
                    Integer userId = userService.registerIfNot(keycloakUserId, fullName);
                    String jsonResponse = String.format("{\"userId\": %d}", userId);
                    this.responseOut.print(jsonResponse);
                    logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    logger.error("Error in login/registration of user " + keycloakUserId);
                    resp.setContentType("text/html");
                    resp.sendError(500, "Error in user login/registration");
                }
            }
            else if (requestMapping("/update")) {
                String userIdString = req.getParameter("userId");
                String requestData = req.getHeader("data");
                JsonObject requestDataJsonObject = new JsonParser().parse(requestData).getAsJsonObject();
                String userBio = requestDataJsonObject.get("bio").getAsString();
                JsonArray tagsJsonArray = requestDataJsonObject.get("selectedTags").getAsJsonArray();
                List<Integer> tagsIds = new ArrayList<>();
                for (int i = 0; i < tagsJsonArray.size(); i++) {
                    tagsIds.add(tagsJsonArray.get(i).getAsInt());
                }
                JsonArray skillsJsonArray = requestDataJsonObject.get("selectedSkills").getAsJsonArray();
                List<Integer> skillsIds = new ArrayList<>();
                for (int i = 0; i < skillsJsonArray.size(); i++) {
                    skillsIds.add(skillsJsonArray.get(i).getAsInt());
                }
                Integer userId = userValidator.getValidatedUserId(userIdString);
                userService.update(userId, userBio, tagsIds, skillsIds);
            }
            else {
                logger.warn("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
        }
        catch (ValidationCommonsException | UserValidatorException e) {
            logger.info(e.getMessage());
            resp.sendError(400, e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            String jsonResponse = "";
            if (requestMapping("/user-info")) {
                String userIdString = req.getParameter("userId");
                Integer userId = userValidator.getValidatedUserId(userIdString);
                User user = userService.getUserInfo(userId);
                if (user != null) {
                    jsonResponse = new Gson().toJson(user);
                    this.responseOut.print(jsonResponse);
                }
                else {
                    String errorMsg = "No user with such id";
                    logger.error(errorMsg);
                    resp.sendError(404, errorMsg);
                }
            }
            else {
                logger.warn("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
            logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
        }
        catch (UserValidatorException e) {
            logger.info(e.getMessage());
            resp.sendError(400, e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}
