package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import java.util.List;

public class OBPCellDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int numberOfColumns;

    private int numberOfRows;

    private String scaned;

    private String inCellName;

    private int inCellyPos;

    private String serialRecordType;

    private String itemName;

    private String itemNo;

    private boolean hasGoods=false;

    private List<OBPCellPositionDTO> obpCellPositions;

    public OBPCellDTO() {
    }

    public OBPCellDTO(OBPCell entity) {
        super(entity);
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public List<OBPCellPositionDTO> getObpCellPositions() {
        return obpCellPositions;
    }

    public void setObpCellPositions(List<OBPCellPositionDTO> obpCellPositions) {
        this.obpCellPositions = obpCellPositions;
    }

    public String getScaned() {
        return scaned;
    }

    public void setScaned(String scaned) {
        this.scaned = scaned;
    }

    public String getInCellName() {
        return inCellName;
    }

    public void setInCellName(String inCellName) {
        this.inCellName = inCellName;
    }

    public int getInCellyPos() {
        return inCellyPos;
    }

    public void setInCellyPos(int inCellyPos) {
        this.inCellyPos = inCellyPos;
    }

    public String getSerialRecordType() {
        return serialRecordType;
    }

    public void setSerialRecordType(String serialRecordType) {
        this.serialRecordType = serialRecordType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isHasGoods() {
        return hasGoods;
    }

    public void setHasGoods(boolean hasGoods) {
        this.hasGoods = hasGoods;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
}
