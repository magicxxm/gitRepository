package com.mushiny.wms.outboundproblem.business.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.utils.ConversionUtil;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForceDeleteShipmentDTO {
    private static final long serialVersionUID = 1L;


    private String shipmentNo;

    private String exSD;

    private String solveDate;

    private String description;

    private String solveBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String timeCondition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String container;

    private int shipmentState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SolveShipmentPositionDTO> solveShipmentPositions = new ArrayList<>();

    public String getShipmentNo() {
        return shipmentNo;
    }

    private String state;

    public String getState() {return state;}

    public void setState(String state) {this.state = state;}

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getExSD() {
        return exSD;
    }

    public void setExSD(String exSD) {
        this.exSD = exSD;
        if (exSD != null)
            this.exSD = exSD.replace("T"," ");
    }

    public String getSolveDate() {
        return solveDate;
    }

    public void setSolveDate(String solveDate) {
        this.solveDate = solveDate.replace("T"," ");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolveBy() {
        return solveBy;
    }

    public void setSolveBy(String solveBy) {
        this.solveBy = solveBy;
    }

    public String getTimeCondition() {
        return timeCondition;
    }

    public void setTimeCondition(String timeCondition) {
        this.timeCondition = timeCondition;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public List<SolveShipmentPositionDTO> getSolveShipmentPositions() {
        return solveShipmentPositions;
    }

    public void setSolveShipmentPositions(List<SolveShipmentPositionDTO> solveShipmentPositions) {
        this.solveShipmentPositions = solveShipmentPositions;
    }

    public int getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(int shipmentState) {
        this.shipmentState = shipmentState;
    }
}
