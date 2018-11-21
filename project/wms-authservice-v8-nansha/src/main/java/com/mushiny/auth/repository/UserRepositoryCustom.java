package com.mushiny.auth.repository;

import com.mushiny.auth.domain.Authority;
import com.mushiny.auth.domain.User;

import java.util.List;

public interface UserRepositoryCustom {

    User getByUsername(String username);

    User getByUsernameAndPassword(String username, String password);

    List<Authority> getAuthoritiesByUserId(String warehouseId, String userId);
}
