package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnArea;

import java.util.ArrayList;
import java.util.List;

public class TurnAreaDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String name;

    private String stationId;

    private String mapId;

    private String sectionId;

    private List<TurnAreaPositionDTO> positions = new ArrayList<>();

    public TurnAreaDTO() {
    }

    public TurnAreaDTO(TurnArea entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public List<TurnAreaPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<TurnAreaPositionDTO> positions) {
        this.positions = positions;
    }
}
