package com.mushiny.wms.system.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SYS_WAREHOUSE")
public class Warehouse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "WAREHOUSE_NO", unique = true, nullable = false)
    private String warehouseNo;

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
            joinColumns = @JoinColumn(name = "WAREHOUSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CLIENT_ID"))
    private Set<Client> clients = new HashSet<>();

    @ManyToMany
    @OrderBy("username")
    @JoinTable(
            name = "SYS_USER_WAREHOUSE",
            joinColumns = @JoinColumn(name = "WAREHOUSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> users = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
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

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
