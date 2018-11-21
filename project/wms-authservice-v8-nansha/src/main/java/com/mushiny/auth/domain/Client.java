package com.mushiny.auth.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SYS_CLIENT")
public class Client extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "CLIENT_NO", unique = true, nullable = false)
    private String number;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "FAX")
    private String fax;

    @ManyToMany(mappedBy = "clients")
    private List<Warehouse> warehouses;

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

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    @Override
    public String toUniqueString() {
        return getNumber();
    }

    @Transient
    public boolean isSystemClient() {
        if (getId().equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}
