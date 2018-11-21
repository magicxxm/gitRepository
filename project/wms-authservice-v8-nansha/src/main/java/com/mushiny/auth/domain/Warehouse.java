package com.mushiny.auth.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SYS_WAREHOUSE")
public class Warehouse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "WAREHOUSE_NO", unique = true, nullable = false)
    private String number;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "FAX")
    private String fax;

    @ManyToMany(mappedBy = "warehouses")
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "WAREHOUSE_CLIENT",
            joinColumns = @JoinColumn(name = "WAREHOUSE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID"))
    private List<Client> clients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toUniqueString() {
        return getNumber();
    }

    @Transient
    public boolean isSystemWarehouse() {
        if (getId().equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}
