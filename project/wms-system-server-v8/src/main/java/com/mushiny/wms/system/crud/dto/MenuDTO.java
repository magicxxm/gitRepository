package com.mushiny.wms.system.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class MenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String childId;

    @NotNull
    private Integer orderIndex;

    private ModuleDTO module;

    private ModuleDTO parentModule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    public ModuleDTO getParentModule() {
        return parentModule;
    }

    public void setParentModule(ModuleDTO parentModule) {
        this.parentModule = parentModule;
    }
}
