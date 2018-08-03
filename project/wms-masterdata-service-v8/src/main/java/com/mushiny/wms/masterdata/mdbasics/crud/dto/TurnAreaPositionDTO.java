package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaPosition;

import javax.validation.constraints.NotNull;

public class TurnAreaPositionDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String name;

    private int turnAreaNodeType;

    private int xPosition;

    private int yPosition;

    private int addressCodeId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String turnAreaId;

    private TurnAreaDTO turnArea;

    public TurnAreaPositionDTO() {
    }

    public TurnAreaPositionDTO(TurnAreaPosition entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTurnAreaNodeType() {
        return turnAreaNodeType;
    }

    public void setTurnAreaNodeType(int turnAreaNodeType) {
        this.turnAreaNodeType = turnAreaNodeType;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getTurnAreaId() {
        return turnAreaId;
    }

    public void setTurnAreaId(String turnAreaId) {
        this.turnAreaId = turnAreaId;
    }

    public TurnAreaDTO getTurnArea() {
        return turnArea;
    }

    public void setTurnArea(TurnAreaDTO turnArea) {
        this.turnArea = turnArea;
    }

    public int getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(int addressCodeId) {
        this.addressCodeId = addressCodeId;
    }
}
