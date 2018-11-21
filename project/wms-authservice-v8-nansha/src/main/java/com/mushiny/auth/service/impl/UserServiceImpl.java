package com.mushiny.auth.service.impl;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.repository.UserRepository;
import com.mushiny.auth.service.ContextService;
import com.mushiny.auth.service.UserService;
import com.mushiny.auth.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private ContextService contextService;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(String id) {
        User user = userRepository.findOne(id);
        return new UserDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        return new UserDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getWithAuthoritiesByUsername(String username) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getWithAuthoritiesById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getWithAuthorities() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public void reloadAuthorities() {
        User callersUser = contextService.getCallersUser();
        Warehouse callersWarehouse = contextService.getCallersWarehouse();
        reloadAuthorities(callersUser.getId(), callersWarehouse.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public void reloadAuthorities(String warehouseId) {
        User callersUser = contextService.getCallersUser();
        reloadAuthorities(callersUser.getId(), warehouseId);
    }

    @Override
    @Transactional(readOnly = true)
    public void reloadAuthorities(String userId, String warehouseId) {
        User updatedUser = userRepository.findOne(userId);
        List<GrantedAuthority> authorities = userRepository.getAuthoritiesByUserId(warehouseId, userId).stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser,
                null, authorities);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }
}
