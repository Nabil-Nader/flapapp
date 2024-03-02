package com.flap.app.security;

import com.flap.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Component
public class JwtTokenUtil {
    public static final String ROLES_CLAIM_KEY = "Title";
    public static final String DELIMITER = ", ";

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String getUsernameFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Set<String> getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);


        return Arrays.stream(claims.get(ROLES_CLAIM_KEY).toString().split(DELIMITER))
                .map(SecurityUtils::convertToAuthorityToString)
                .collect(Collectors.toSet());
    }

    public boolean isTokenValid(String token, String userDetails) {
        final String username = getUsernameFromJWT(token);
        return (username.equals(userDetails)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String generateToken(User user) {
        return doGenerateToken(user);
    }

    private String doGenerateToken(User user) {
        String userId = String.valueOf(user.getId());
        Claims claims = Jwts.claims().setSubject(userId);

        Set<SimpleGrantedAuthority> auth = getAuthority(user.getRole().toString().toLowerCase());


        String authorities = auth.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));

        return Jwts.builder()
                .setClaims(claims)
                .setId(String.valueOf(user.getId()))
                .claim(ROLES_CLAIM_KEY, Collections.singletonList(authorities))
                .setIssuer("inventory")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    private Set<SimpleGrantedAuthority> getAuthority(String role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;

    }


    public Authentication getAuthentication(UserDetails userDetails, String token, HttpServletRequest request) {
        Claims claims = extractAllClaims(token);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get(ROLES_CLAIM_KEY).toString().split("[,]"))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return userPasswordAuthToken;
    }


}
