package com.mushiny.wms.internaltool.web.dto;

import com.mushiny.wms.internaltool.common.domain.StorageLocation;

import java.io.Serializable;
import java.math.BigDecimal;

public class StorageLocationAmountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal itemDataAmount = BigDecimal.ZERO;

    private String id;

    private String name;

    public StorageLocationAmountDTO() {
    }

    public StorageLocationAmountDTO(StorageLocation storageLocation) {
        if (storageLocation != null) {
            this.id = storageLocation.getId();
            this.name = storageLocation.getName();
        }
    }

    public BigDecimal getItemDataAmount() {
        return itemDataAmount;
    }

    public void setItemDataAmount(BigDecimal itemDataAmount) {
        this.itemDataAmount = itemDataAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
