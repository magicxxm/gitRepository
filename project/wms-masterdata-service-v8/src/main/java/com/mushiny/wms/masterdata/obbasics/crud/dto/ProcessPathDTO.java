package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;

import javax.validation.constraints.NotNull;

public class ProcessPathDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    private String description;
    private String regenerateShortedPicks;
    private int minShipmentsPerBatch = 0;
    private int maxShipmentsPerBatch = 0;
    private int minItemsPerBatch = 0;
    private int maxItemsPerBatch = 0;
    private String collateDocuments;
    private int targetPickRate = 0;
    private int processPad = 0;
    private int batchLimit = 0;
    private int toteLimit = 0;
    private Boolean isHotpick=false;
    @NotNull
    private String pickWay;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickDestinationId;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinDestinationId;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinWallTypeId;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String packDestinationId;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String processPathTypeId;

    private PickStationTypeDTO pickDestination;

    private ReBinStationTypeDTO rebinDestination;

    private ReBinWallTypeDTO rebinWallType;

    private PackingStationTypeDTO packDestination;

    private ProcessPathTypeDTO processPathType;

    public ProcessPathDTO() {
    }

    public ProcessPathDTO(ProcessPath entity) {
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

    public String getRegenerateShortedPicks() {
        return regenerateShortedPicks;
    }

    public void setRegenerateShortedPicks(String regenerateShortedPicks) {
        this.regenerateShortedPicks = regenerateShortedPicks;
    }

    public int getMinShipmentsPerBatch() {
        return minShipmentsPerBatch;
    }

    public void setMinShipmentsPerBatch(int minShipmentsPerBatch) {
        this.minShipmentsPerBatch = minShipmentsPerBatch;
    }

    public int getMaxShipmentsPerBatch() {
        return maxShipmentsPerBatch;
    }

    public void setMaxShipmentsPerBatch(int maxShipmentsPerBatch) {
        this.maxShipmentsPerBatch = maxShipmentsPerBatch;
    }

    public int getMinItemsPerBatch() {
        return minItemsPerBatch;
    }

    public void setMinItemsPerBatch(int minItemsPerBatch) {
        this.minItemsPerBatch = minItemsPerBatch;
    }

    public int getMaxItemsPerBatch() {
        return maxItemsPerBatch;
    }

    public void setMaxItemsPerBatch(int maxItemsPerBatch) {
        this.maxItemsPerBatch = maxItemsPerBatch;
    }

    public String getCollateDocuments() {
        return collateDocuments;
    }

    public void setCollateDocuments(String collateDocuments) {
        this.collateDocuments = collateDocuments;
    }

    public String getRebinDestinationId() {
        return rebinDestinationId;
    }

    public void setRebinDestinationId(String rebinDestinationId) {
        this.rebinDestinationId = rebinDestinationId;
    }

    public String getRebinWallTypeId() {
        return rebinWallTypeId;
    }

    public void setRebinWallTypeId(String rebinWallTypeId) {
        this.rebinWallTypeId = rebinWallTypeId;
    }

    public String getPackDestinationId() {
        return packDestinationId;
    }

    public void setPackDestinationId(String packDestinationId) {
        this.packDestinationId = packDestinationId;
    }

    public String getProcessPathTypeId() {
        return processPathTypeId;
    }

    public void setProcessPathTypeId(String processPathTypeId) {
        this.processPathTypeId = processPathTypeId;
    }

    public ReBinStationTypeDTO getRebinDestination() {
        return rebinDestination;
    }

    public void setRebinDestination(ReBinStationTypeDTO rebinDestination) {
        this.rebinDestination = rebinDestination;
    }

    public ReBinWallTypeDTO getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(ReBinWallTypeDTO rebinWallType) {
        this.rebinWallType = rebinWallType;
    }

    public PackingStationTypeDTO getPackDestination() {
        return packDestination;
    }

    public void setPackDestination(PackingStationTypeDTO packDestination) {
        this.packDestination = packDestination;
    }

    public ProcessPathTypeDTO getProcessPathType() {
        return processPathType;
    }

    public void setProcessPathType(ProcessPathTypeDTO processPathType) {
        this.processPathType = processPathType;
    }

    public int getTargetPickRate() {
        return targetPickRate;
    }

    public void setTargetPickRate(int targetPickRate) {
        this.targetPickRate = targetPickRate;
    }

    public int getProcessPad() {
        return processPad;
    }

    public void setProcessPad(int processPad) {
        this.processPad = processPad;
    }

    public int getBatchLimit() {
        return batchLimit;
    }

    public void setBatchLimit(int batchLimit) {
        this.batchLimit = batchLimit;
    }

    public String getPickDestinationId() {
        return pickDestinationId;
    }

    public void setPickDestinationId(String pickDestinationId) {
        this.pickDestinationId = pickDestinationId;
    }

    public PickStationTypeDTO getPickDestination() {
        return pickDestination;
    }

    public void setPickDestination(PickStationTypeDTO pickDestination) {
        this.pickDestination = pickDestination;
    }

    public Boolean getHotpick() {
        return isHotpick;
    }

    public void setHotpick(Boolean hotpick) {
        isHotpick = hotpick;
    }

    public int getToteLimit() {
        return toteLimit;
    }

    public void setToteLimit(int toteLimit) {
        this.toteLimit = toteLimit;
    }

    public String getPickWay() {
        return pickWay;
    }

    public void setPickWay(String pickWay) {
        this.pickWay = pickWay;
    }
}
