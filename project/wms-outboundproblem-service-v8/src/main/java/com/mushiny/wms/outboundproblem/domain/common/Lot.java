package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "INV_LOT", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "LOT_NO", "ITEMDATA_ID"
        })
})
public class Lot extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "LOT_NO", nullable = false)
    private String lotNo;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOT_DATE", nullable = false)
    private LocalDate lotDate;

    @Column(name = "USE_NOT_AFTER")
    private LocalDate useNotAfter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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

    public LocalDate getLotDate() {
        return lotDate;
    }

    public void setLotDate(LocalDate lotDate) {
        this.lotDate = lotDate;
    }

    public LocalDate getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(LocalDate useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }
}
