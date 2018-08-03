package com.mushiny.wms.system.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.system.domain.Resource;

import javax.validation.constraints.NotNull;

public class ResourceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String resourceKey;

    @NotNull
    private String locale;

    private String resourceValue;

    public ResourceDTO() {
    }

    public ResourceDTO(Resource entity) {
        super(entity);
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(String resourceValue) {
        this.resourceValue = resourceValue;
    }
}
