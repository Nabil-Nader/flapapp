package com.flap.app.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flap.app.dto.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.BindException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Log4j2
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException ex, HttpServletRequest request) {

        log.error("error with a message " + ex.getMessage() + " cause " + ex.getCause());

        String r = ex.getMessage();
        String x = "";
        if (r.contains("Access Denied")) {
            request.setAttribute("Access Denied", r);
            request.getRequestURI();
            x = request.getRequestURI();
            request.setAttribute("client api", x);
        }
        return new Response<>(request.getRequestDispatcher("requestDispatcherPath"), ex.getMessage(), false, HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleMissingHeaderException(MissingRequestHeaderException ex, HttpServletRequest request) {
        // Customize the response here
        String errorMessage = "Header is missing: " + ex.getHeaderName();
        log.error("error with a Header: " + errorMessage);

        return new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleBindException(BindException ex, HttpServletRequest request) {
        // Customize the response here
        String errorMessage = "Bind is exception: " + ex.getCause();
        log.error("error with a bind exception " + ex.getMessage() + " cause " + ex.getCause());

        return new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);
        log.error("error with a MethodArgumentNotValidException " + errorMessage);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, 400), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String errorMessage = "Method not allowed. Supported methods are: " +
                String.join(", ", Objects.requireNonNull(ex.getSupportedMethods()));
        log.error("error with a method not allowed: " + errorMessage);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, 405), HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        String errorMessage = "Authentication failed: " + ex.getMessage();
        log.error("error with a Authentication: " + errorMessage);
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        String errorMessage = "Bad credentials: " + ex.getMessage();
        log.error("error with a Bad credentials: " + errorMessage);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), ex.getMessage(), false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String errorMessage = "Access denied: " + ex.getMessage();
        log.error("error with a Access denied: " + errorMessage);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<Response> handleJwtTokenException(JwtTokenException ex, HttpServletRequest request) {
        String errorMessage = "JWT token error: " + ex.getMessage();
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        String errorMessage = "Expired JWT token: " + ex.getMessage();
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Response> handleGeneralJwtException(JwtException ex, HttpServletRequest request) {
        String errorMessage = "Invalid JWT token: " + ex.getMessage();
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Response> handleTokenNotFoundException(AuthenticationCredentialsNotFoundException ex, HttpServletRequest request) {
        String errorMessage = "Token is required but not provided: " + ex.getMessage();
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = "Required request body is missing";
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<Response> handlingOtpEXception(OtpException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), ex.getMessage(), false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Response> handleNullPointException(IllegalStateException ex, HttpServletRequest request) {
        String errorMessage = "you sent a null for string" + ex.getMessage();
        log.error("Caught IllegalArgumentException: " + ex.getMessage(), ex);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        String errorMessage = "you sent a null for string" + ex.getMessage();
        log.error("Caught IllegalArgumentException: " + ex.getMessage(), ex);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<Response> handleTokenMissingException(TokenMissingException ex, HttpServletRequest request) {
        log.error("Caught TokenMissingException: " + ex.getMessage(), ex);
        String errorMessage = "Token is missing: " + ex.getMessage();
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Response> handleTokenExpiredException(Exception ex, HttpServletRequest request) {
        log.error("Caught unexpected exception: " + ex.getMessage());
        return new ResponseEntity<>(new Response<>(request.getRequestURI(), ex.getMessage(), false, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }




    @ExceptionHandler(JwtFilterException.class)
    public ResponseEntity<Response> handleJwtFilterException(JwtFilterException ex, HttpServletRequest request) {
        String errorMessage = "Exception in JwtAuthenticationFilter: " + ex.getMessage();
        log.error(errorMessage, ex);

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), errorMessage, false, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Response> handleUserNotFoundxception(UserNotFound ex, HttpServletRequest request) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(new Response<>(request.getRequestURI(), ex.getMessage(), false, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}


