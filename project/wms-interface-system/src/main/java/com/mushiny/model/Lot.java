package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "INV_LOT")
public class Lot extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "LOT_NO", nullable = false)
    private String number;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOT_DATE", nullable = false)
    private LocalDate date;

    @Column(name = "PRODUCT_DATE")
    private LocalDate productDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @Column(name = "USE_NOT_AFTER")
//    private Instant bestBeforeEnd;
    private LocalDate bestBeforeEnd;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getProductDate() {
        return productDate;
    }

    public void setProductDate(LocalDate productDate) {
        this.productDate = productDate;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public LocalDate getBestBeforeEnd() {
        return bestBeforeEnd;
    }

    public void setBestBeforeEnd(LocalDate bestBeforeEnd) {
        this.bestBeforeEnd = bestBeforeEnd;
    }

}
