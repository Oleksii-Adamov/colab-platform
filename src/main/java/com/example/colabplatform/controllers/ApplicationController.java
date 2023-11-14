package com.example.colabplatform.controllers;

import com.example.colabplatform.enitities.Application;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.ProjectValidatorException;
import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;
import com.example.colabplatform.infoClasses.ApplicationInfo;
import com.example.colabplatform.services.ApplicationService;
import com.example.colabplatform.services.ProjectService;
import com.example.colabplatform.validators.ApplicationValidator;
import com.example.colabplatform.validators.ProjectValidator;
import com.example.colabplatform.validators.UserValidator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController extends AbstractController {

    private static final ApplicationService applicationService = new ApplicationService();
    private static final ProjectValidator projectValidator = new ProjectValidator();
    private static final UserValidator userValidator = new UserValidator();

    private static final ApplicationValidator applicationValidator = new ApplicationValidator();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            if (requestMapping("/create")) {
                String userIdString = req.getParameter("userId");
                String projectIdString = req.getParameter("projectId");
                logger.info("Creating application, userId: " + userIdString + " projectId: " + projectIdString);
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                Integer applicationId = applicationService.create(userId, projectId);
                String jsonResponse = String.format("{\"ApplicationId\": %d}", applicationId);
                this.responseOut.print(jsonResponse);
                logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
            }
            else if (requestMapping("/approve")) {
                String applicationIdString = req.getParameter("applicationId");
                String userIdString = req.getParameter("userId");
                String projectIdString = req.getParameter("projectId");
                logger.info("Approving application, userId: " + userIdString + " projectId: " + projectIdString +
                        " applicationId: "+ applicationIdString);
                Integer applicationId = applicationValidator.getValidatedApplicationId(applicationIdString);
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                applicationService.approve(applicationId, userId, projectId);
            }
            else if (requestMapping("/reject")) {
                String applicationIdString = req.getParameter("applicationId");
                logger.info("Rejecting application, applicationId: " + applicationIdString);
                Integer applicationId = applicationValidator.getValidatedApplicationId(applicationIdString);
                applicationService.reject(applicationId);
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
            if (requestMapping("/project-applications")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                List<ApplicationInfo> applicationInfos = applicationService.getProjectApplications(projectId);
                jsonResponse = new Gson().toJson(applicationInfos);
                this.responseOut.print(jsonResponse);
            }
            else {
                logger.warn("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
            logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
        }
        catch (ValidationCommonsException e) {
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
