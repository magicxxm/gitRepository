package com.mushiny.auth.service;

import com.mushiny.auth.service.dto.UserDTO;

public interface SecurityService {

    String getLoggedInUsername();

    UserDTO login(String username, String password);
}
