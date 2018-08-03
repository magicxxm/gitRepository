package com.mushiny.wms.tot.general.domain;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SYS_CLIENT")
public class Client extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "CLIENT_NO", unique = true, nullable = false)
    private String clientNo;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "FAX")
    private String fax;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "SYS_WAREHOUSE_CLIENT",
            joinColumns = @JoinColumn(name = "CLIENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "WAREHOUSE_ID"))
    private Set<Warehouse> warehouses = new HashSet<>();

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public boolean isSystemClient() {
        // 判断用户是否拥有此仓库的SYSTEM客户
        return this.getId().equals(Constant.SYSTEM_CLIENT);
    }
}
