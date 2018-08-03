package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReceiveCategoryDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private int orderIndex = 0;

    @NotNull
    private String categoryType;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveDestinationId;

    private ReceiveDestinationDTO receiveDestination;

    private List<ReceiveCategoryPositionDTO> positions = new ArrayList<>();

    public ReceiveCategoryDTO() {
    }

    public ReceiveCategoryDTO(ReceiveCategory entity) {
        super(entity);
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

    public List<ReceiveCategoryPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveCategoryPositionDTO> positions) {
        this.positions = positions;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getReceiveDestinationId() {
        return receiveDestinationId;
    }

    public void setReceiveDestinationId(String receiveDestinationId) {
        this.receiveDestinationId = receiveDestinationId;
    }

    public ReceiveDestinationDTO getReceiveDestination() {
        return receiveDestination;
    }

    public void setReceiveDestination(ReceiveDestinationDTO receiveDestination) {
        this.receiveDestination = receiveDestination;
    }
}
