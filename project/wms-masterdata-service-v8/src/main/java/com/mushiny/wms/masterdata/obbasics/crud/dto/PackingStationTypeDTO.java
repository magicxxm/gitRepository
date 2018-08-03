package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PackingStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    private String packStationType;
    @NotNull
    private String isScanBoxType;
    private String description;

    private Boolean ifScan;

    private Boolean ifPrint;

    private Boolean ifScanInvoice;

    private Boolean ifWeight;

    private List<PackingStationDTO> packingStations = new ArrayList<>();

    private List<PackingStationTypePositionDTO> positions = new ArrayList<>();

    public PackingStationTypeDTO() {
    }

    public PackingStationTypeDTO(PackingStationType entity) {
        super(entity);
    }

    public Boolean getIfScan() {
        return ifScan;
    }

    public void setIfScan(Boolean ifScan) {
        this.ifScan = ifScan;
    }

    public Boolean getIfPrint() {
        return ifPrint;
    }

    public void setIfPrint(Boolean ifPrint) {
        this.ifPrint = ifPrint;
    }

    public Boolean getIfScanInvoice() {
        return ifScanInvoice;
    }

    public void setIfScanInvoice(Boolean ifScanInvoice) {
        this.ifScanInvoice = ifScanInvoice;
    }

    public Boolean getIfWeight() {
        return ifWeight;
    }

    public void setIfWeight(Boolean ifWeight) {
        this.ifWeight = ifWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackStationType() {
        return packStationType;
    }

    public void setPackStationType(String packStationType) {
        this.packStationType = packStationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PackingStationDTO> getPackingStations() {
        return packingStations;
    }

    public void setPackingStations(List<PackingStationDTO> packingStations) {
        this.packingStations = packingStations;
    }

    public List<PackingStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PackingStationTypePositionDTO> positions) {
        this.positions = positions;
    }

    public String getIsScanBoxType() {
        return isScanBoxType;
    }

    public void setIsScanBoxType(String isScanBoxType) {
        this.isScanBoxType = isScanBoxType;
    }
}


