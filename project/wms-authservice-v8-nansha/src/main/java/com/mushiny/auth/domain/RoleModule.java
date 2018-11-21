package com.mushiny.auth.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_ROLE_MODULE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ROLE_ID", "MODULE_ID"})
})
public class RoleModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

    @Id
    @Column(name = "MODULE_ID", nullable = false)
    private String moduleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleModule that = (RoleModule) o;

        if (!roleId.equals(that.roleId)) return false;
        return moduleId.equals(that.moduleId);
    }

    @Override
    public int hashCode() {
        int result = roleId.hashCode();
        result = 31 * result + moduleId.hashCode();
        return result;
    }
}
