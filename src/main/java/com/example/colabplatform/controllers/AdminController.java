package com.example.colabplatform.controllers;

import com.example.colabplatform.enitities.Collaborator;
import com.example.colabplatform.enitities.Project;
import com.example.colabplatform.exceptions.UserValidatorException;
import com.example.colabplatform.exceptions.ValidationCommonsException;
import com.example.colabplatform.infoClasses.AdminStats;
import com.example.colabplatform.infoClasses.CollaboratorProjectInfo;
import com.example.colabplatform.infoClasses.ProjectStats;
import com.example.colabplatform.services.AdminService;
import com.example.colabplatform.services.ProjectService;
import com.example.colabplatform.validators.ValidationCommons;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AdminController extends AbstractController {

    private static final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            String jsonResponse = "";
            if (requestMapping("/stats")) {
                String beginningMonthStr = req.getParameter("beginningMonth");
                Integer beginningMonth = ValidationCommons.getValidatedMonth(beginningMonthStr);
                String beginningYearStr = req.getParameter("beginningYear");
                Integer beginningYear = ValidationCommons.getValidatedYear(beginningYearStr);
                String endMonthStr = req.getParameter("endMonth");
                Integer endMonth = ValidationCommons.getValidatedMonth(endMonthStr);
                String endYearStr = req.getParameter("endYear");
                Integer endYear = ValidationCommons.getValidatedYear(endYearStr);
                AdminStats adminStats = new AdminStats(
                        adminService.getNewUsersStatsInRange(beginningMonth, beginningYear, endMonth, endYear),
                        adminService.getProjectsStatsInRange(beginningMonth, beginningYear, endMonth, endYear),
                        adminService.getNewCollaboratorsStatsInRange(beginningMonth, beginningYear, endMonth, endYear));
                jsonResponse = new Gson().toJson(adminStats);
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
