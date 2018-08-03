package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataSerialNumber;

import javax.validation.constraints.NotNull;

public class ItemDataSerialNumberDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String itemdata;

    @NotNull
    private String serialNo;

    public ItemDataSerialNumberDTO(ItemDataSerialNumber entity) {
        super(entity);
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getItemdata() {
        return itemdata;
    }

    public void setItemdata(String itemdata) {
        this.itemdata = itemdata;
    }
}
