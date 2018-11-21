package com.mushiny.auth.service.impl;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.repository.UserRepository;
import com.mushiny.auth.security.SecurityUtils;
import com.mushiny.auth.service.SecurityService;
import com.mushiny.auth.service.dto.UserDTO;
import com.mushiny.auth.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @Override
    public String getLoggedInUsername() {
        return SecurityUtils.getCurrentUsername();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.debug(String.format("Login %s successfully!", username));

            User user = userRepository.getByUsername(username);
            return userMapper.mapEntityIntoDTO(user);
        }
        return null;
    }
}
