package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_CHARGER")
public class ChargingPile extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "PLACEMARK")
    private int placeMark;
    @Column(name = "CHARGER_ID")
    private int chargerId;
    @Column(name = "CHARGER_TYPE")
    private int chargerType;
    @Column(name = "TOWARD")
    private int toWard;
    @Column(name = "STATE")
    private String state;
    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }

    public int getToWard() {
        return toWard;
    }

    public void setToWard(int toWard) {
        this.toWard = toWard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Section getSection() {
        return section;
    }

    public int getChargerId() {
        return chargerId;
    }

    public void setChargerId(int chargerId) {
        this.chargerId = chargerId;
    }

    public int getChargerType() {
        return chargerType;
    }

    public void setChargerType(int chargerType) {
        this.chargerType = chargerType;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
