package com.mushiny.auth.security;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Inject
    private UserRepository userRepository;

    @Override
    public User getCurrentAuditor() {
        String username = SecurityUtils.getCurrentUsername();
        if (username != null) {
            return userRepository.getByUsername(username);
        }

        return null;
    }
}
