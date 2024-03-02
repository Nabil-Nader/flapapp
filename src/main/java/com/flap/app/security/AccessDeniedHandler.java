package com.flap.app.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Log4j2
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.warn("--## User: " + username + " attempted to access the protected URL: " + request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "API_DENIED_ACCESS");
    }
}

