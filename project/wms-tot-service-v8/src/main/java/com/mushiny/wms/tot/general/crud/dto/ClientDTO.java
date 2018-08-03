package com.mushiny.wms.tot.general.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.general.domain.Client;

import javax.validation.constraints.NotNull;

public class ClientDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String clientNo;

    @NotNull
    private String name;

    private String email;

    private String phone;

    private String fax;

    public ClientDTO() {
    }

    public ClientDTO(Client entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
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
