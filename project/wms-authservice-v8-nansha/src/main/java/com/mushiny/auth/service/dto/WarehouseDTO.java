package com.mushiny.auth.service.dto;

import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.service.exception.VersionException;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class WarehouseDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String number;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String fax;

    @Size(max = 255)
    private String fromIP;

    @Size(max = 255)
    private String toIP;

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

    public String getFromIP() {
        return fromIP;
    }

    public void setFromIP(String fromIP) {
        this.fromIP = fromIP;
    }

    public String getToIP() {
        return toIP;
    }

    public void setToIP(String toIP) {
        this.toIP = toIP;
    }

    public WarehouseDTO() {
        super();
    }

    public WarehouseDTO(Warehouse warehouse) {
        super(warehouse);

        this.name = warehouse.getName();
        this.number = warehouse.getNumber();
        this.email = warehouse.getEmail();
        this.phone = warehouse.getPhone();
        this.fax = warehouse.getFax();
    }

    public void merge(Warehouse warehouse) throws VersionException {
        super.merge(warehouse);

        warehouse.setName(this.name);
        warehouse.setNumber(this.number);
        warehouse.setEmail(this.email);
        warehouse.setPhone(this.phone);
        warehouse.setFax(this.fax);
    }
}
