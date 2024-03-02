package com.flap.app.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Log4j2

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        log.info("doFilterInternal -> checking for for header logs: ");

        String apiKeyHeader = req.getHeader("api_key");
        String requestUrl = req.getRequestURI();

        String authToken;
        String userId;
        Set<String> role;

        if (requestUrl.contains("/api/users" + "/login") || requestUrl.contains("/api/users/add") || requestUrl.contains("/h2-console")) {
            filterChain.doFilter(req, res);
        } else {
            try {
                authToken = apiKeyHeader;
                userId = jwtTokenUtil.getUsernameFromJWT(authToken);
                role = jwtTokenUtil.getRolesFromToken(authToken);

                log.info("User Id obtained from JWT token " + userId);

                if (Boolean.TRUE.equals(jwtTokenUtil.isTokenValid(authToken, userId))) {
                    log.info(String.format("authenticated user %s, setting security context", userId));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, getAllAuthorities(role));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    req.setAttribute("userId", userId);
                    req.setAttribute("role", role);
                } else {
                    log.debug("Invalid JWT token");
                }
                log.info("doFilterInternal -> done and we are entering filter chain");
            } catch (MalformedJwtException | UnsupportedJwtException | SignatureException |
                     IllegalArgumentException e) {
                log.error("an error occurred during getting username from token :" + e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("The token has been expired and not valid anymore :" + e.getMessage());
            }
            // Move the following line inside the else block
            filterChain.doFilter(req, res);
        }
    }

    private Collection<SimpleGrantedAuthority> getAllAuthorities(Set<String> tokenRole) {

        return tokenRole.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

    }


}
