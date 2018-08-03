package com.mushiny.wms.system.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_USER_WAREHOUSE_ROLE")
@IdClass(UserWarehouseRolePK.class)
public class UserWarehouseRole implements Serializable {
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
    public String toString() {
        return "UserWarehouseRole{" +
                "userId='" + userId + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
