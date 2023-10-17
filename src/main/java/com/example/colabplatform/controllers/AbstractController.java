package com.example.colabplatform.controllers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;


public abstract class AbstractController extends HttpServlet {

    protected HttpServletRequest req;
    protected HttpServletResponse resp;

    protected PrintWriter responseOut;

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.req = req;
        this.resp = resp;
        this.responseOut = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    protected boolean requestMapping(String regex) {
        String pathInfo = req.getPathInfo();
        if (Pattern.matches(regex, pathInfo)) {
            return resp.getStatus() >= 200 && resp.getStatus() <= 299;
        }
        return false;
    }
}
