package com.mushiny.wms.report.domain;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "INV_UNITLOAD")
public class UnitLoad extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "LABEL", unique = true, nullable = false)
    private String label;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "TYPE_ID")
//    private UnitLoadType type;

    @Column(name = "STOCKTAKING_DATE")
    private LocalDateTime stockTakingDate;

//    @ManyToOne
//    @JoinColumn(name = "STORAGELOCATION_ID")
//    private StorageLocation storageLocation;

    @Column(name = "WEIGHT", precision = 16, scale = 3)
    private BigDecimal weight = BigDecimal.ZERO;

    @Column(name = "WEIGHT_CALCULATED", precision = 16, scale = 3)
    private BigDecimal weightCalculated = BigDecimal.ZERO;

    @Column(name = "WEIGHT_MEASURE", precision = 16, scale = 3)
    private BigDecimal weightMeasure = BigDecimal.ZERO;

    @Column(name = "LOCATION_INDEX", nullable = false)
    private int locationIndex = -1;

    @Column(name = "CARRIER", nullable = false)
    private boolean carrier = false;

    @Column(name = "STATION_NAME")
    private String stationName;

    @ManyToOne
    @JoinColumn(name = "CARRIERUNITLOAD_ID")
    private UnitLoad carrierUnitLoad;

//    @ManyToMany
//    @OrderBy("customerNo")
//    @JoinTable(
//            name = "INV_UNITLOAD_SHIPMENT",
//            joinColumns = @JoinColumn(name = "UNITLOAD_ID"),
//            inverseJoinColumns = @JoinColumn(name = "SHIPMENT_ID"))
//    private List<CustomerShipment> shipments = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

//    public UnitLoadType getType() {
//        return type;
//    }
//
//    public void setType(UnitLoadType type) {
//        this.type = type;
//    }

    public LocalDateTime getStockTakingDate() {
        return stockTakingDate;
    }

    public void setStockTakingDate(LocalDateTime stockTakingDate) {
        this.stockTakingDate = stockTakingDate;
    }

//    public StorageLocation getStorageLocation() {
//        return storageLocation;
//    }
//
//    public void setStorageLocation(StorageLocation storageLocation) {
//        this.storageLocation = storageLocation;
//    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public UnitLoad getCarrierUnitLoad() {
        return carrierUnitLoad;
    }

    public void setCarrierUnitLoad(UnitLoad carrierUnitLoad) {
        this.carrierUnitLoad = carrierUnitLoad;
    }

//    public List<CustomerShipment> getShipments() {
//        return shipments;
//    }
//
//    public void setShipments(List<CustomerShipment> shipments) {
//        this.shipments = shipments;
//    }
}
