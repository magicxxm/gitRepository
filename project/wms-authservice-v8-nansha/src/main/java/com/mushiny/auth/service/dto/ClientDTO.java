package com.mushiny.auth.service.dto;

import com.mushiny.auth.domain.Client;
import com.mushiny.auth.service.exception.VersionException;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClientDTO extends BaseDTO {

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

    public ClientDTO(Client client) {
        super(client);

        this.number = client.getNumber();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.fax = client.getFax();
    }

    public void merge(Client client) throws VersionException {
        super.merge(client);

        client.setName(this.name);
        client.setNumber(this.number);
        client.setEmail(this.email);
        client.setPhone(this.phone);
        client.setFax(this.fax);
    }
}
