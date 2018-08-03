package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
//import com.mushiny.wms.masterdata.ibbasics.domain.ReceivingProcessContainer;
//import com.mushiny.wms.masterdata.mdbasics.crud.dto.ContainerDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReceivingProcessContainerDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String state;

    private BigDecimal amount;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receivingStationId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String containerId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receivingDestinationId;

    private ReceiveStationDTO receivingStation;

//    private ContainerDTO container;

    private ReceiveDestinationDTO receivingDestination;

    public ReceivingProcessContainerDTO() {
    }

//    public ReceivingProcessContainerDTO(ReceivingProcessContainer receivingProcessContainer) {
//        super(receivingProcessContainer);
//    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getReceivingDestinationId() {
        return receivingDestinationId;
    }

    public void setReceivingDestinationId(String receivingDestinationId) {
        this.receivingDestinationId = receivingDestinationId;
    }

//    public ContainerDTO getContainer() {
//        return container;
//    }
//
//    public void setContainer(ContainerDTO container) {
//        this.container = container;
//    }

    public ReceiveDestinationDTO getReceivingDestination() {
        return receivingDestination;
    }

    public void setReceivingDestination(ReceiveDestinationDTO receivingDestination) {
        this.receivingDestination = receivingDestination;
    }

    public String getReceivingStationId() {
        return receivingStationId;
    }

    public void setReceivingStationId(String receivingStationId) {
        this.receivingStationId = receivingStationId;
    }

    public ReceiveStationDTO getReceivingStation() {
        return receivingStation;
    }

    public void setReceivingStation(ReceiveStationDTO receivingStation) {
        this.receivingStation = receivingStation;
    }
}
