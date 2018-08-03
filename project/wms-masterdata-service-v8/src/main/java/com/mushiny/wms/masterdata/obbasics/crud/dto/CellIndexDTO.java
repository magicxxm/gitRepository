package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.CellIndex;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallType;

import javax.validation.constraints.NotNull;

public class CellIndexDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private int number;

    @NotNull
    private int cellIndex;
    @NotNull
    private PickPackWallTypeDTO pickPackWallType;

    public CellIndexDTO() {
    }

    public CellIndexDTO(CellIndex entity) {
        super(entity);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public PickPackWallTypeDTO getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallTypeDTO pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

}
