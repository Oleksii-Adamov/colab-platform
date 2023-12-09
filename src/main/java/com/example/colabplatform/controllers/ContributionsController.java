package com.example.colabplatform.controllers;

import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;
import com.example.colabplatform.infoClasses.ContributionInfo;
import com.example.colabplatform.services.ContributionService;
import com.example.colabplatform.validators.ContributionValidator;
import com.example.colabplatform.validators.ProjectValidator;
import com.example.colabplatform.validators.UserValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ContributionsController extends AbstractController {
    private static final ContributionService contributionService = new ContributionService();
    private static final ProjectValidator projectValidator = new ProjectValidator();
    private static final UserValidator userValidator = new UserValidator();

    private static final ContributionValidator contributionValidator = new ContributionValidator();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            if (requestMapping("/create")) {
                String userIdString = req.getParameter("userId");
                String projectIdString = req.getParameter("projectId");
                String requestData = req.getHeader("data");
                JsonObject requestDataJsonObject = new JsonParser().parse(requestData).getAsJsonObject();
                String contributionDescription = requestDataJsonObject.get("contributionDescription").getAsString();
                Integer contributionValue = requestDataJsonObject.get("contributionValue").getAsInt();
                logger.info("Creating contribution, userId: " + userIdString + " projectId: " + projectIdString);
                Integer userId = userValidator.getValidatedUserId(userIdString);
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                Integer contributionId = contributionService.create(userId, projectId, contributionDescription, contributionValue);
                String jsonResponse = String.format("{\"ContributionId\": %d}", contributionId);
                this.responseOut.print(jsonResponse);
                logger.info("response from " + req.getRequestURI() + " " + jsonResponse);
            }
            else if (requestMapping("/approve")) {
                String contributionIdString = req.getParameter("contributionId");
                logger.info("Approving contribution " +  contributionIdString);
                Integer contributionId = contributionValidator.getValidatedContributionId(contributionIdString);
                contributionService.approve(contributionId);
            }
            else if (requestMapping("/reject")) {
                String contributionIdString = req.getParameter("contributionId");
                logger.info("Rejecting contribution " +  contributionIdString);
                Integer contributionId = contributionValidator.getValidatedContributionId(contributionIdString);
                contributionService.reject(contributionId);
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
            if (requestMapping("/project-pending-contributions")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                List<ContributionInfo> contributionInfos = contributionService.getProjectPendingContributions(projectId);
                jsonResponse = new Gson().toJson(contributionInfos);
                this.responseOut.print(jsonResponse);
            }
            else if (requestMapping("/project-approved-contributions")) {
                String projectIdString = req.getParameter("projectId");
                Integer projectId = projectValidator.getValidatedProjectId(projectIdString);
                List<ContributionInfo> contributionInfos = contributionService.getProjectApprovedContributions(projectId);
                jsonResponse = new Gson().toJson(contributionInfos);
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
