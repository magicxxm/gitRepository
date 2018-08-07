package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.*;

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

}
