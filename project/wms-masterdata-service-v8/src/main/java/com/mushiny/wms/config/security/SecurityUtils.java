package com.mushiny.wms.config.security;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
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
            }else {
                return "system";
            }
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());
        }
//        throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());
    }
}
