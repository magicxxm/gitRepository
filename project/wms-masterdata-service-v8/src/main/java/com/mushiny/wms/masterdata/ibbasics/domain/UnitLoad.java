package com.mushiny.wms.masterdata.ibbasics.domain;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
//import com.mushiny.wms.masterdata.mdbasics.domain.Container;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "INV_UNITLOAD")
public class UnitLoad extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "LABEL", unique = true, nullable = false)
    private String label;
    @Column(name = "STATION_NAME")
     private String stationName;
    @ManyToOne
    @JoinColumn(name = "STORAGELOCATION_ID")
    private StorageLocation storageLocation;

//    @ManyToOne
//    @JoinColumn(name = "CONTAINER_ID")
//    private Container container;

//    @Column(name = "PACKAGE_TYPE")
//    private String packageType;

    @Column(name = "STOCKTAKING_DATE")
    private ZonedDateTime stockTakingDate;

    @Column(name = "WEIGHT_CALCULATED", precision = 16, scale = 3)
    private BigDecimal weightCalculated = BigDecimal.ZERO;

    @Column(name = "WEIGHT_MEASURE", precision = 16, scale = 3)
    private BigDecimal weightMeasure = BigDecimal.ZERO;

    @Column(name = "WEIGHT", precision = 16, scale = 3)
    private BigDecimal weight = BigDecimal.ZERO;

//    @Column(name = "OPENED", nullable = false)
//    private boolean opened = false;

    @Column(name = "CARRIER", nullable = false)
    private boolean carrier = false;

    @Column(name = "LOCATION_INDEX")
    private int locationIndex = -1;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "TYPE_ID")
//    private UnitLoadType type;

    @Column(name = "CARRIERUNITLOAD_ID", insertable = false, updatable = false)
    private String carrierUnitLoadId;

    @ManyToOne
    @JoinColumn(name = "CARRIERUNITLOAD_ID")
    private UnitLoad carrierUnitLoad;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

//    public Container getContainer() {
//        return container;
//    }
//
//    public void setContainer(Container container) {
//        this.container = container;
//    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public ZonedDateTime getStockTakingDate() {
        return stockTakingDate;
    }

    public void setStockTakingDate(ZonedDateTime stockTakingDate) {
        this.stockTakingDate = stockTakingDate;
    }

    public BigDecimal getWeightCalculated() {
        return weightCalculated;
    }

    public void setWeightCalculated(BigDecimal weightCalculated) {
        this.weightCalculated = weightCalculated;
    }

    public BigDecimal getWeightMeasure() {
        return weightMeasure;
    }

    public void setWeightMeasure(BigDecimal weightMeasure) {
        this.weightMeasure = weightMeasure;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
//
//    public boolean isOpened() {
//        return opened;
//    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

//
//    public void setOpened(boolean opened) {
//        this.opened = opened;
//    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

//    public UnitLoadType getType() {
//        return type;
//    }
//
//    public void setType(UnitLoadType type) {
//        this.type = type;
//    }

    public String getCarrierUnitLoadId() {
        return carrierUnitLoadId;
    }

    public void setCarrierUnitLoadId(String carrierUnitLoadId) {
        this.carrierUnitLoadId = carrierUnitLoadId;
    }

    public UnitLoad getCarrierUnitLoad() {
        return carrierUnitLoad;
    }

    public void setCarrierUnitLoad(UnitLoad carrierUnitLoad) {
        this.carrierUnitLoad = carrierUnitLoad;
    }

//    public String getPackageType() {
//        return packageType;
//    }
//
//    public void setPackageType(String packageType) {
//        this.packageType = packageType;
//    }
}
