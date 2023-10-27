package com.example.colabplatform.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserIdentificationFilter implements Filter {

    private Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = LogManager.getLogger(UserIdentificationFilter.class);
        logger.info("UserIdentificationFilter initialized");
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
                logger.info(jwt.getClaim("sub").asString());
                logger.info(jwt.getClaim("name").asString());
                httpRequest.setAttribute("userId", jwt.getClaim("sub").asString());
                httpRequest.setAttribute("fullName", jwt.getClaim("name").asString());
                chain.doFilter(httpRequest, httpResponse);
            } catch (Exception e) {
                logger.error(e.getMessage());
                httpResponse.sendError(401, "Invalid access token");
            }
        }
        else {
            logger.warn("UserIdentificationFilter can filter only http request and response");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // no resources to free
    }
}
