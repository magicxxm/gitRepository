package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_ITEMUNIT")
public class ItemUnit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "UNIT_TYPE", nullable = false)
    private String unitType;

    @Column(name = "BASE_FACTOR", nullable = false)
    private int baseFactor;

    @ManyToOne
    @JoinColumn(name = "BASEUNIT_ID")
    private ItemUnit baseUnit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getBaseFactor() {
        return baseFactor;
    }

    public void setBaseFactor(int baseFactor) {
        this.baseFactor = baseFactor;
    }

    public ItemUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(ItemUnit baseUnit) {
        this.baseUnit = baseUnit;
    }
}
