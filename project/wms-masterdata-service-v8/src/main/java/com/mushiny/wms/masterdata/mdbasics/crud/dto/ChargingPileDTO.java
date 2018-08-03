package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ChargingPile;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Section;

import javax.validation.constraints.NotNull;

public class ChargingPileDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private int placeMark;
    private int chargerId;
    private int chargerType;

    private int toWard;

    private String state;
    @NotNull
    private String sectionId;
    private SectionDTO section;

    public ChargingPileDTO() {
    }

    public ChargingPileDTO(ChargingPile entity) {
        super(entity);
    }

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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
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
}
