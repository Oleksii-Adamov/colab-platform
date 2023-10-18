package com.example.colabplatform.controllers;

import com.example.colabplatform.dao.DAOFactory;
import com.example.colabplatform.enitities.Project;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestController extends AbstractController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
            String jsonResponse = "";
//            List<String> arr = new ArrayList<>();
//            arr.add("Test1");
//            arr.add("Test2");
//            List<Project> projects = new ArrayList<>();
//            projects.add(new Project(1, "pr1"));
//            projects.add(new Project(2, "pr2"));
            List<Project> projects = DAOFactory.getInstance().getprojectDAO().getProjects();
            jsonResponse = new Gson().toJson(projects);
            this.responseOut.print(jsonResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.sendError(500, e.getMessage());
        }
        this.responseOut.flush();
    }
}
