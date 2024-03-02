package com.flap.app.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtils {

    public static SimpleGrantedAuthority convertToAuthority(String role) {
        String x = role.replace("[", "");
        String y = x.replace("]", "");
        return new SimpleGrantedAuthority(y);
    }

    public static String convertToAuthorityToString(String role) {
        String x = role.replace("[", "");
        return x.replace("]", "");
    }
}
