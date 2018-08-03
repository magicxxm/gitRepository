package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DigitalLabelDTO;

import javax.validation.constraints.NotNull;

public class WorkStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String positionNo;

    private int positionIndex;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String digitalLabelId;

    private DigitalLabelDTO digitalLabel;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workstationId;

    private WorkStationDTO workstation;

    public WorkStationPositionDTO() {
    }

    public WorkStationPositionDTO(WorkStationPosition entity) {
        super(entity);
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getDigitalLabelId() {
        return digitalLabelId;
    }

    public void setDigitalLabelId(String digitalLabelId) {
        this.digitalLabelId = digitalLabelId;
    }

    public DigitalLabelDTO getDigitalLabel() {
        return digitalLabel;
    }

    public void setDigitalLabel(DigitalLabelDTO digitalLabel) {
        this.digitalLabel = digitalLabel;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public WorkStationDTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkStationDTO workstation) {
        this.workstation = workstation;
    }
}
