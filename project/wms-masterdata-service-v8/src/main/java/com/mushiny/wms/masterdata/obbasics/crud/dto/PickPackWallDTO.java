package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWall;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PickPackWallDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private int numberOfColumns = 0;

    @NotNull
    private int numberOfRows = 0;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private PickPackWallTypeDTO pickPackWallType;

    private List<String> digitalLabel1 = new ArrayList<String>();

    private List<String> digitalLabel2 = new ArrayList<String>();

    private List pickPackFieldTypeNames = new ArrayList();

    private String storageLocationTypeId;

    private String areaId;


    public PickPackWallDTO() {

    }

    public PickPackWallDTO(PickPackWall entity) {
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public PickPackWallTypeDTO getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallTypeDTO pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

    public List getPickPackFieldTypeNames() {
        return pickPackFieldTypeNames;
    }

    public void setPickPackFieldTypeNames(List pickPackFieldTypeNames) {
        this.pickPackFieldTypeNames = pickPackFieldTypeNames;
    }

    public List<String> getDigitalLabel1() {
        return digitalLabel1;
    }

    public void setDigitalLabel1(List<String> digitalLabel1) {
        this.digitalLabel1 = digitalLabel1;
    }

    public List<String> getDigitalLabel2() {
        return digitalLabel2;
    }

    public void setDigitalLabel2(List<String> digitalLabel2) {
        this.digitalLabel2 = digitalLabel2;
    }

    public String getStorageLocationTypeId() {
        return storageLocationTypeId;
    }

    public void setStorageLocationTypeId(String storageLocationTypeId) {
        this.storageLocationTypeId = storageLocationTypeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
