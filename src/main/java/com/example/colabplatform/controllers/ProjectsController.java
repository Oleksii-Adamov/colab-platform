package com.example.colabplatform.controllers;

import com.example.colabplatform.exceptions.ProjectValidatorException;
import com.example.colabplatform.services.ProjectService;
import com.example.colabplatform.validators.ProjectValidator;
import com.example.colabplatform.validators.UserValidator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsController extends AbstractController {
    private static final ProjectService projectService = new ProjectService();
    private static final ProjectValidator projectValidator = new ProjectValidator();
    private static final UserValidator userValidator = new UserValidator();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            if (requestMapping("/create")) {
                String name = req.getParameter("name");
                String userIdString = req.getParameter("userId");
                String requestData = req.getHeader("data");
                JsonObject requestDataJsonObject = new JsonParser().parse(requestData).getAsJsonObject();
                String projectDescription = requestDataJsonObject.get("projectDescription").getAsString();
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
                logger.info("Creating project " + name + " " + userIdString);
                logger.info(requestData);
                logger.info(projectDescription);
                logger.info(tagsIds);
                logger.info(skillsIds);
                Integer userId;
                try {
                    projectValidator.validateName(name);
                    userId = userValidator.getValidatedUserId(userIdString);
                }  catch (ProjectValidatorException e) {
                    logger.info(e.getMessage());
                    resp.sendError(400, e.getMessage());
                    return;
                }
                Integer createdId = projectService.create(name, userId);
                String jsonResponse = String.format("{\"ProjectId\": %d}", createdId);
                this.responseOut.print(jsonResponse);
                logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
            }
            else if (requestMapping("/join")) {

            }
            else {
                logger.warn("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}