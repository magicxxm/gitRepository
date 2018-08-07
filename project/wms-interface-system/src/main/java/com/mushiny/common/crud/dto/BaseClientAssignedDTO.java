package com.mushiny.common.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;

public class BaseClientAssignedDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId;

    private String client;

    public BaseClientAssignedDTO() {
    }

    public BaseClientAssignedDTO(BaseEntity baseEntity) {
        super(baseEntity);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
