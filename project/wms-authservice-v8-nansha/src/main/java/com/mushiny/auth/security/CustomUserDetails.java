package com.mushiny.auth.security;

import com.mushiny.auth.domain.Authority;
import com.mushiny.auth.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private User user;

    private String userName;
    private String password;

    private List<SimpleGrantedAuthority> authorities;

    public User getUser() {
        return user;
    }

    public CustomUserDetails(User user) {
        this.authorities = new ArrayList<>();
        this.user = user;
        this.userName = user.getUsername();
        this.password = user.getPassword();
        if (user.getAuthorities() != null) {
            for (Authority authority : user.getAuthorities()) {
                if (authority != null) {
                    this.authorities.add(new SimpleGrantedAuthority(authority.getName()));
                }
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
