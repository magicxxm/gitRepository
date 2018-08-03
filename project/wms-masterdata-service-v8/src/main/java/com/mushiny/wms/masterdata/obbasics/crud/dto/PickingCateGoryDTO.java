package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.obbasics.common.exception.VersionException;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;

import java.util.ArrayList;
import java.util.List;

public class PickingCateGoryDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private ProcessPathDTO processPath;

    private String processPathId;

    private int orderIndex = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId;

    private List<PickingCateGoryPositionDTO> positions = new ArrayList<>();

    public PickingCateGoryDTO() {
    }

    public PickingCateGoryDTO(PickingCateGory pickingCategory) {
        super(pickingCategory);
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

    public ProcessPathDTO getProcessPath() {
        return processPath;
    }

    public void setProcessPath(ProcessPathDTO processPath) {
        this.processPath = processPath;
    }

    public String getProcessPathId() {
        return processPathId;
    }

    public void setProcessPathId(String processPathId) {
        this.processPathId = processPathId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public List<PickingCateGoryPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingCateGoryPositionDTO> positions) {
        this.positions = positions;
    }

    public void merge(PickingCateGory pickingCategory) throws VersionException {
        super.merge(pickingCategory);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
