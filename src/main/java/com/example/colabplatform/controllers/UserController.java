package com.example.colabplatform.controllers;

import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.services.UserService;
import com.example.colabplatform.validators.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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
                    logger.info("Sent " + String.format("{\"userId\": %d}", userId));
                    this.responseOut.print(String.format("{\"userId\": %d}", userId));
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    logger.error("Error in login/registration of user " + keycloakUserId);
                    resp.setContentType("text/html");
                    resp.sendError(500, "Error in user login/registration");
                }
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
