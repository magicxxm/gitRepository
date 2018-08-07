package com.mushiny.config.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUsername() {
        /*SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = null;
        try {
            if (authentication != null) {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    username = springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    username = (String) authentication.getPrincipal();
                }
            }
            if (username != null) {
                return username;
            }
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());
        }
        throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;
        if (authentication != null) {
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                currentUsername = authentication.getName();
            }
        }

        return currentUsername == null ? "system" : currentUsername;
    }
}
