package com.example.colabplatform.controllers;

import com.example.colabplatform.exceptions.ProjectValidatorException;
import com.example.colabplatform.services.ProjectService;
import com.example.colabplatform.validators.ProjectValidator;
import com.example.colabplatform.validators.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                //String requestData = req.getReader().lines().collect(Collectors.joining());
//                StringBuilder sb = new StringBuilder();
//                BufferedReader reader = req.getReader();
//                try {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line).append('\n');
//                    }
//                } finally {
//                    reader.close();
//                }
//                String requestData = sb.toString();
                String requestData = req.getHeader("data");
                logger.info("Creating project " + name + " " + userIdString);
                logger.info(requestData);
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
