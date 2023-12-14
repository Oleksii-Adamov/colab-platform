package com.example.colabplatform.controllers;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.ProjectValidatorException;
import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;
import com.example.colabplatform.infoClasses.ProjectStats;
import com.example.colabplatform.services.ProjectService;
import com.example.colabplatform.validators.ProjectValidator;
import com.example.colabplatform.validators.UserValidator;
import com.example.colabplatform.validators.ValidationCommons;
import com.google.gson.*;

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
                projectValidator.validateName(name);
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer createdId = projectService.create(name, userId, projectDescription, tagsIds, skillsIds);
                String jsonResponse = String.format("{\"ProjectId\": %d}", createdId);
                this.responseOut.print(jsonResponse);
                logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
            }
            else if (requestMapping("/rate")) {
                String projectIdString = req.getParameter("projectId");
                String userIdString = req.getParameter("userId");
                String ratingString = req.getParameter("rating");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer rating = projectValidator.getValidatedProjectRating(ratingString);
                projectService.rateProject(projectId, userId, rating);
            }
            else if (requestMapping("/update")) {
                String projectIdString = req.getParameter("projectId");
                String name = req.getParameter("name");
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
                projectValidator.validateName(name);
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                projectService.updateProject(projectId, name, projectDescription, tagsIds, skillsIds);
            }
            else if (requestMapping("/finish")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                projectService.markAsFinished(projectId);
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
            if (requestMapping("/projects-user-in")) {
                String userIdString = req.getParameter("userId");
                Integer userId = userValidator.getValidatedUserId(userIdString);
                List<CollaboratorProjectInfo> projects = projectService.getProjectsUserIn(userId);
                jsonResponse = new Gson().toJson(projects);
                this.responseOut.print(jsonResponse);
            }
            else if (requestMapping("/project-info")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                Project project = projectService.getProjectInfo(projectId);
                if (project != null) {
                    jsonResponse = new Gson().toJson(project);
                    this.responseOut.print(jsonResponse);
                }
                else {
                    String errorMsg = "No project with such id";
                    logger.error(errorMsg);
                    resp.sendError(500, errorMsg);
                }
            }
            else if (requestMapping("/project-recommendations")) {
                String userIdString = req.getParameter("userId");
                Integer userId = userValidator.getValidatedUserId(userIdString);
                List<Project> projects = projectService.getRecommendedProjects(userId);
                jsonResponse = new Gson().toJson(projects);
                this.responseOut.print(jsonResponse);
            }
            else if (requestMapping("/auth")) {
                String userIdString = req.getParameter("userId");
                Integer userId = userValidator.getValidatedUserId(userIdString);
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                Collaborator collaborator = projectService.getCollaborator(userId, projectId);
                if (collaborator != null) {
                    jsonResponse = new Gson().toJson(collaborator);
                    this.responseOut.print(jsonResponse);
                }
                else {
                    String msg = "You are not collaborator";
                    logger.info(msg);
                    jsonResponse = String.format("{\"msg\": \"%s\"}", msg);
                    this.responseOut.print(jsonResponse);
                }
            }
            else if (requestMapping("/user-rating")) {
                logger.info("/user-rating");
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                String userIdString = req.getParameter("userId");
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer rating = projectService.getUserRating(projectId, userId);
                if (rating != null) {
                    jsonResponse = String.format("{\"rating\": %d}", rating);
                    this.responseOut.print(jsonResponse);
                }
                else {
                    jsonResponse = "{}";
                    this.responseOut.print(jsonResponse);
                    logger.info("Not found rating with this project AND user ID");
                }
            }
            else if (requestMapping("/stats")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                String beginningMonthStr = req.getParameter("beginningMonth");
                Integer beginningMonth = ValidationCommons.getValidatedMonth(beginningMonthStr);
                String beginningYearStr = req.getParameter("beginningYear");
                Integer beginningYear = ValidationCommons.getValidatedYear(beginningYearStr);
                String endMonthStr = req.getParameter("endMonth");
                Integer endMonth = ValidationCommons.getValidatedMonth(endMonthStr);
                String endYearStr = req.getParameter("endYear");
                Integer endYear = ValidationCommons.getValidatedYear(endYearStr);
                ProjectStats projectStats = new ProjectStats(
                        projectService.getContributionStatsInRange(projectId, beginningMonth, beginningYear, endMonth, endYear),
                        projectService.getNewUsersStatsInRange(projectId, beginningMonth, beginningYear, endMonth, endYear));
                jsonResponse = new Gson().toJson(projectStats);
                this.responseOut.print(jsonResponse);
            }
            else {
                logger.warn("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
            logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
        }
        catch (UserValidatorException | ValidationCommonsException e) {
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
