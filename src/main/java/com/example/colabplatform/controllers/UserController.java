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
                boolean success = true;
                try {
                    userService.registerIfNot(keycloakUserId, fullName);
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    success = false;
                }

                if (success) {
                    logger.info("success");
                    resp.setStatus(200);
                    this.responseOut.flush();
                }
                else {
                    logger.error("Error in registration of user " + keycloakUserId);
                    resp.setContentType("text/html");
                    resp.sendError(500, "Error in user registration");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}
