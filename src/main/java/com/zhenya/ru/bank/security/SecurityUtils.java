package com.zhenya.ru.bank.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class SecurityUtils {
     private SecurityContext securityContext;

    public boolean isValidLogin(String username) {
        securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            throw new SecurityException("Unauthorized!");
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return !principal.getUsername().equals(username);
    }
}
