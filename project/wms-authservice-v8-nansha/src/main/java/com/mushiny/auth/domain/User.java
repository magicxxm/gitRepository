package com.mushiny.auth.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "SYS_USER")
public class User extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    private static String LOCALE_DEFAULT = "EN";

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOCALE")
    private String locale = LOCALE_DEFAULT;

    @Lob
    @Column(name = "AVATAR")
    private byte[] avatar;

    @ManyToOne
    @JoinColumn(name = "USERGROUP_ID")
    private UserGroup userGroup;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "SYS_USER_WAREHOUSE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "WAREHOUSE_ID", referencedColumnName = "ID"))
    private List<Warehouse> warehouses;

    @Transient
    private List<Authority> authorities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase(Locale.ENGLISH);
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public List<Warehouse> getWarehouses() {
        if (this.warehouses == null) {
            this.warehouses = new ArrayList<>();
        }
        return this.warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean hasWarehouse(String number) {
        for (Warehouse wh : getWarehouses()) {
            if (wh.getNumber().equals(number))
                return true;
        }
        return false;
    }

    public boolean hasWarehouse(Warehouse warehouse) {
        for (Warehouse wh : getWarehouses()) {
            if (wh.equals(warehouse))
                return true;
        }
        return false;
    }

    @Override
    public String toUniqueString() {
        return getName();
    }

    @Override
    public String toShortString() {
        return super.toShortString() + "[username=" + username + "]";
    }
}
