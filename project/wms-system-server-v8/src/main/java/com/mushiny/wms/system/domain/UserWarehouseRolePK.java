package com.mushiny.wms.system.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class UserWarehouseRolePK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Id
    @Column(name = "WAREHOUSE_ID", nullable = false)
    private String warehouseId;

    @Id
    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWarehouseRolePK pk = (UserWarehouseRolePK) o;

        return userId != null ? userId.equals(pk.userId) : pk.userId == null && (warehouseId != null ? warehouseId.equals(pk.warehouseId) : pk.warehouseId == null && (roleId != null ? roleId.equals(pk.roleId) : pk.roleId == null));

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (warehouseId != null ? warehouseId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserWarehouseRole{" +
                "userId='" + userId + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
