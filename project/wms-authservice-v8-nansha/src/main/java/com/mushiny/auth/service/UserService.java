package com.mushiny.auth.service;

import com.mushiny.auth.service.dto.UserDTO;

public interface UserService {

    UserDTO getById(String id);

    UserDTO getByUsername(String username);

    UserDTO getWithAuthoritiesByUsername(String username);

    UserDTO getWithAuthoritiesById(String id);

    UserDTO getWithAuthorities();

    void reloadAuthorities();

    void reloadAuthorities(String warehouseId);

    void reloadAuthorities(String userId, String warehouseId);
}
