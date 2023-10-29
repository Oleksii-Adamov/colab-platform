package com.example.colabplatform.controllers;

import com.example.colabplatform.services.SkillService;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SkillsController extends AbstractController {
    private static final SkillService skillService = new SkillService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            String jsonResponse = "";
            if (requestMapping("/get")) {
                jsonResponse = new Gson().toJson(skillService.getAll());
                this.responseOut.print(jsonResponse);
            }
            else {
                logger.info("No such path " + req.getRequestURI());
                resp.sendError(404, "No such path " + req.getRequestURI());
                return;
            }
            logger.info("response from " + req.getRequestURI() + " " + jsonResponse);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}
