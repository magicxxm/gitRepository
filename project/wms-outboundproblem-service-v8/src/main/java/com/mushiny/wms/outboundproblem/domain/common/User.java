package com.mushiny.wms.outboundproblem.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SYS_USER")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "USERNAME", unique = true, nullable = false, updatable = false)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOCALE")
    private String locale;

    @Column(name = "AVATAR")
    private String avatar;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WAREHOUSE_ID")
    private Warehouse warehouse;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

//    @OneToMany(mappedBy = "operator")
//    private Collection<WorkStation> workStation;
//
//    public Collection<WorkStation> getWorkStation() {
//        return workStation;
//    }
//
//    public void setWorkStation(Collection<WorkStation> workStation) {
//        this.workStation = workStation;
//    }
}
