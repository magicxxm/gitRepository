package com.mushiny.wms.masterdata.general.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;
import com.mushiny.wms.masterdata.ibbasics.domain.StowThreshold;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "USERGROUP_ID")
    private UserGroup userGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WAREHOUSE_ID")
    private Warehouse warehouse;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "SYS_USER_WAREHOUSE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "WAREHOUSE_ID"))
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "IB_RECEIVEELIGIBILITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECEIVETHRESHOLD_ID"))
    private Set<ReceiveThreshold> receiveThresholds = new HashSet<>();

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "IB_STOWELIGIBILITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "STOWTHRESHOLD_ID"))
    private Set<StowThreshold> stowThresholds = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "OB_PICKINGAREAELIGIBILITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PICKINGAREA_ID"))
    private List<PickingArea> pickingArea = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "OB_PICKINGPROCESSELIGIBILITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROCESSPATH_ID"))
    private List<ProcessPath> processPath = new ArrayList<>();

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

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
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

    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public List<PickingArea> getPickingArea() {
        return pickingArea;
    }

    public void setPickingArea(List<PickingArea> pickingArea) {
        this.pickingArea = pickingArea;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Set<ReceiveThreshold> getReceiveThresholds() {
        return receiveThresholds;
    }

    public void setReceiveThresholds(Set<ReceiveThreshold> receiveThresholds) {
        this.receiveThresholds = receiveThresholds;
    }

    public List<ProcessPath> getProcessPath() {
        return processPath;
    }

    public void setProcessPath(List<ProcessPath> processPath) {
        this.processPath = processPath;
    }

    public Set<StowThreshold> getStowThresholds() {
        return stowThresholds;
    }

    public void setStowThresholds(Set<StowThreshold> stowThresholds) {
        this.stowThresholds = stowThresholds;
    }
}
