package com.mushiny.auth.web.controller;

import com.mushiny.auth.security.SecurityUtils;
import com.mushiny.auth.service.SecurityService;
import com.mushiny.auth.service.UserService;
import com.mushiny.auth.service.dto.UserDTO;
import com.mushiny.auth.web.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Inject
    private UserService userService;

    @Inject
    private SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginVM loginVM) {
        return Optional.ofNullable(securityService.login(loginVM.getUsername(), loginVM.getPassword()))
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/account")
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getWithAuthoritiesByUsername(SecurityUtils.getCurrentUsername()))
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        return request.getRemoteUser();
    }

    @PostMapping("/switch-warehouse")
    public ResponseEntity<?> switchWarehouse(@RequestBody String warehouseId) {
        userService.reloadAuthorities(warehouseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
