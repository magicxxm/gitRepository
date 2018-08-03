package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PACKINGSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class PackingStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PACK_STATION_TYPE")
    private String packStationType;
    @Column(name = "IS_SCAN_BOX_TYPE", nullable = false)
    private String isScanBoxType;
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IF_SCAN")
    private Boolean ifScan;

    @Column(name = "IF_PRINT")
    private Boolean ifPrint;

    @Column(name = "IF_SCAN_INVOICE")
    private Boolean ifScanInvoice;

    @Column(name = "IF_WEIGHT")
    private Boolean ifWeight;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "packingStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PackingStationTypePosition> positions = new ArrayList<>();

    public void addPosition(PackingStationTypePosition position) {
        getPositions().add(position);
        position.setPackingStationType(this);
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

    public List<PackingStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PackingStationTypePosition> positions) {
        this.positions = positions;
    }

    public String getIsScanBoxType() {
        return isScanBoxType;
    }

    public void setIsScanBoxType(String isScanBoxType) {
        this.isScanBoxType = isScanBoxType;
    }
}
