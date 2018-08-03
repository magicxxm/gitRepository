package com.mushiny.wms.system.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "SYS_SELECTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "SELECTION_KEY", "SELECTION_VALUE"
        })
})
public class Selection extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SELECTION_KEY", nullable = false)
    private String selectionKey;

    @Column(name = "SELECTION_VALUE")
    private String selectionValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex;

    @Column(name = "RESOURCE_KEY")
    private String resourceKey;

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
