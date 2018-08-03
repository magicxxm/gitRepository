package com.mushiny.wms.masterdata.general.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.general.domain.User;

import javax.validation.constraints.NotNull;

public class UserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String phone;

    private String email;

    private String locale;

    private String avatar;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userGroupId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String warehouseId;

    private UserGroupDTO userGroup;

    private ClientDTO client;

    private WarehouseDTO warehouse;

    public UserDTO() {
    }

    public UserDTO(User entity) {
        super(entity);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UserGroupDTO getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupDTO userGroup) {
        this.userGroup = userGroup;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }
}
