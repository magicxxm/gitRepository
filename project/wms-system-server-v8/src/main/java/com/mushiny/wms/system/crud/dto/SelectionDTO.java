package com.mushiny.wms.system.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.system.domain.Selection;

import javax.validation.constraints.NotNull;

public class SelectionDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String selectionKey;

    private String selectionValue;

    private String description;

    @NotNull
    private int orderIndex;

    private String resourceKey;

    public SelectionDTO() {
    }

    public SelectionDTO(Selection entity) {
        super(entity);
    }

    public String getSelectionKey() {
        return selectionKey;
    }

    public void setSelectionKey(String selectionKey) {
        this.selectionKey = selectionKey;
    }

    public String getSelectionValue() {
        return selectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        this.selectionValue = selectionValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }
}
