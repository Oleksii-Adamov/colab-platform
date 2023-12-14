package com.example.colabplatform.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
public class AdminFilter implements Filter {
    private Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = LogManager.getLogger(AdminFilter.class);
        logger.info("AdminFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String authorizationHeader = httpRequest.getHeader("Authorization");
            String token = authorizationHeader.substring("Bearer ".length());
            try {
                final DecodedJWT jwt = JWT.decode(token);
                JSONArray jsonArray = new JSONArray(jwt.getClaim("realm_access").asMap().get("roles").toString());
                Iterator<Object> iterator = jsonArray.iterator();
                List<String> roles = new ArrayList<>();
                while(iterator.hasNext()) {
                    roles.add((String) iterator.next());
                }
                if (roles.contains("Admin")) {
                    chain.doFilter(httpRequest, httpResponse);
                }
                else {
                    httpResponse.sendError(401, "Access denied, you are not admin");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                httpResponse.sendError(401, "Invalid access token");
            }
        }
        else {
            logger.warn("AdminFilter can filter only http request and response");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // no resources to free
    }
}
