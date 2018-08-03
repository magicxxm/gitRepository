package com.mushiny.wms.tot.general.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.general.domain.UserGroup;

import javax.validation.constraints.NotNull;

public class UserGroupDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    public UserGroupDTO() {
    }

    public UserGroupDTO(UserGroup entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
