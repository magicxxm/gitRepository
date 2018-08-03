package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_ITEMDATA_SERIALNUMBER")
public class ItemDataSerialNumber extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL_NO", nullable = false)
    private String serialNo;

    @Column(name = "ITEMDATA_ID")
    private String itemData;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }
}
