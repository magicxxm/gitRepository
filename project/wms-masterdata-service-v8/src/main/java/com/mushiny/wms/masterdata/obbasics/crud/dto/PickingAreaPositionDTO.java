package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ZoneDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingAreaPosition;

import javax.validation.constraints.NotNull;

public class PickingAreaPositionDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String positionNo;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickingAreaId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zoneId;

    private ZoneDTO zone;

    private PickingAreaDTO pickingArea;

    public PickingAreaPositionDTO() {
    }

    public PickingAreaPositionDTO(PickingAreaPosition entity) {
        super(entity);
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPickingAreaId() {
        return pickingAreaId;
    }

    public void setPickingAreaId(String pickingAreaId) {
        this.pickingAreaId = pickingAreaId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    public PickingAreaDTO getPickingArea() {
        return pickingArea;
    }

    public void setPickingArea(PickingAreaDTO pickingArea) {
        this.pickingArea = pickingArea;
    }
}
