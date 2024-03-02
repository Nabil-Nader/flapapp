package com.flap.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flap.app.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

import static jakarta.servlet.http.HttpServletResponse.*;

@Log4j2
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Responding with unauthorized error. Message - {}", authException.getMessage());

        Response errorResponse = new Response<>();
        response.setContentType("application/json");

        if (request.getAttribute("expiredJWT") != null) {
            handleErrorResponse(response, errorResponse, "Token expired", 401, SC_UNAUTHORIZED, request.getRequestURI());

        } else if (request.getAttribute("invalidToken") != null) {
            handleErrorResponse(response, errorResponse, "Invalid token", 401, SC_UNAUTHORIZED, request.getRequestURI());

        } else if (request.getAttribute("Access Denied" ) !=null) {

            Object message = request.getAttribute("Access Denied");
            Object api = request.getAttribute("client api");

                handleErrorResponse(response, errorResponse, "Access Denied", 403, SC_FORBIDDEN, api.toString());




        } else {
            handleErrorResponse(response, errorResponse, "Something bad happened", 401, SC_UNAUTHORIZED, request.getRequestURI());
        }
    }

    private void handleErrorResponse(HttpServletResponse response, Response errorResponse, String message,
                                     int status, int httpResponseStatus, String requestURI) throws IOException {
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        errorResponse.setSuccess(false);
        errorResponse.setData(requestURI);

        response.setStatus(httpResponseStatus);
        response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
    }

}