package com.mushiny.auth.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_USER_WAREHOUSE_ROLE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "WAREHOUSE_ID", "ROLE_ID"})
})
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWarehouseRole that = (UserWarehouseRole) o;

        if (!userId.equals(that.userId)) return false;
        if (!warehouseId.equals(that.warehouseId)) return false;
        return roleId.equals(that.roleId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + warehouseId.hashCode();
        result = 31 * result + roleId.hashCode();
        return result;
    }
}
