package com.example.colabplatform.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectController extends AbstractController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            if (requestMapping("/create")) {
                String name = req.getParameter("name");
                String userId = req.getParameter("userId");
//                try {
//                    courseValidator.validateName(name);
//                }  catch (ProjectValidatorException e) {
//                    logger.info(e.getMessage());
//                    resp.sendError(400, e.getMessage());
//                    return;
//                }
//                Integer createdId = courseService.create(name, userId, maxGrade);
//                this.responseOut.print(String.format("{id: %d}", createdId));
            }
            else if (requestMapping("/join")) {
//                Integer courseId;
//                try {
//                    courseId = courseValidator.getValidatedCourseId(req.getParameter("courseId"));
//                }
//                catch (ProjectValidatorException e) {
//                    logger.info(e.getMessage());
//                    resp.sendError(400, e.getMessage());
//                    return;
//                }
//                courseService.joinCourse(userId, courseId);
            }
            else {
                logger.info("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}
