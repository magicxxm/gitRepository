package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.UnitLoadDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class StorageLocationDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String name;

    private int xPos = 0;

    private int yPos = 0;

    private int zPos = 0;

    private String field;

    private int fieldIndex = 0;

    private int orderIndex = 0;

    private String scanCode;

    private BigDecimal allocation;

    private int allocationState = 0;

    private LocalDateTime stocktakingDate;

    private String face;

    private String color;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String areaId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clusterId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zoneId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dropZoneId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podId;

    private StorageLocationTypeDTO storageLocationType;

    private AreaDTO area;

    private ZoneDTO zone;

    private DropZoneDTO dropZone;

    private PodDTO Pod;

    private SectionDTO section;
    //为了显示商品数量
    private int number;
    //为了显示工作站名字
    private String stationName;


    public StorageLocationDTO() {
    }

    public StorageLocationDTO(StorageLocation entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDropZoneId() {
        return dropZoneId;
    }

    public void setDropZoneId(String dropZoneId) {
        this.dropZoneId = dropZoneId;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getzPos() {
        return zPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }

    public int getAllocationState() {
        return allocationState;
    }

    public void setAllocationState(int allocationState) {
        this.allocationState = allocationState;
    }

    public LocalDateTime getStocktakingDate() {
        return stocktakingDate;
    }

    public void setStocktakingDate(LocalDateTime stocktakingDate) {
        this.stocktakingDate = stocktakingDate;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public StorageLocationTypeDTO getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationTypeDTO storageLocationType) {
        this.storageLocationType = storageLocationType;
    }

    public AreaDTO getArea() {
        return area;
    }

    public void setArea(AreaDTO area) {
        this.area = area;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    public DropZoneDTO getDropZone() {
        return dropZone;
    }

    public void setDropZone(DropZoneDTO dropZone) {
        this.dropZone = dropZone;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public PodDTO getPod() {
        return Pod;
    }

    public void setPod(PodDTO pod) {
        Pod = pod;
    }

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

}
