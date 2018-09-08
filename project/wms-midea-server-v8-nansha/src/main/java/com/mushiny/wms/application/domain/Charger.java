package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MD_CHARGER")
public class Charger extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PLACEMARK")
    private Integer placeMark;

    @Column(name = "STATE")
    private String state;
    @Column(name = "CHARGER_TYPE")
    private int chargerType;
    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public int getChargerType() {
        return chargerType;
    }

    public void setChargerType(int chargerType) {
        this.chargerType = chargerType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(Integer placeMark) {
        this.placeMark = placeMark;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
