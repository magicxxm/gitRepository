package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_ITEMDATA_SERIALNUMBER")
public class ItemDataSerialNumber extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL_NO", nullable = false)
    private String serialNo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }
}
