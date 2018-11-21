package com.mushiny.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushiny.auth.domain.Authority;
import com.mushiny.auth.domain.User;
import com.mushiny.auth.service.exception.VersionException;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    public static final int EMAIL_MIN_LENGTH = 4;
    public static final int EMAIL_MAX_LENGTH = 255;

    public static final int UUID_LENGTH = 36;

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @NotNull
    @Size(min = 1, max = 255)
    private String username;

    @NotNull
    @Size(min = 1, max = 255)
    @JsonIgnore
    private String password;

    @NotNull
    @Size(min = 1, max = 255)
    @JsonIgnore
    private String passwordConfirmation;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String locale;

    private List<WarehouseDTO> warehouses;

    private List<Authority> authorities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public List<WarehouseDTO> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<WarehouseDTO> warehouses) {
        this.warehouses = warehouses;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public UserDTO(User user) {
        super(user);
    }

    public void merge(User user) throws VersionException {
        super.merge(user);
    }
}
