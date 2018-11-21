package com.mushiny.auth.security;

import com.mushiny.auth.domain.Authority;
import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.repository.UserRepository;
import com.mushiny.auth.service.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private ContextService contextService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.getByUsername(username))
                .map(user -> {
                    String warehouseId = "";
                    Warehouse warehouse = contextService.getCurrentWarehouse();
                    if (warehouse != null) {
                        warehouseId = warehouse.getId();
                    }
                    List<Authority> authorities = userRepository.getAuthoritiesByUserId(warehouseId, user.getId());
                    user.setAuthorities(authorities);
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException(username + " was not found in the database"));
    }
}
