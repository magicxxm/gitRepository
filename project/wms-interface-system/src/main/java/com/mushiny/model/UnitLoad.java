package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

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
    private String labelId;

    @ManyToOne
    @JoinColumn(name = "STORAGELOCATION_ID")
    private StorageLocation storageLocation;

    @Column(name = "WEIGHT", precision = 16, scale = 3)
    private BigDecimal weight = BigDecimal.ZERO;

    @Column(name = "WEIGHT_CALCULATED", precision = 16, scale = 3)
    private BigDecimal weightCalculated = BigDecimal.ZERO;

    @Column(name = "WEIGHT_MEASURE", precision = 16, scale = 3)
    private BigDecimal weightMeasure = BigDecimal.ZERO;

    @Column(name = "STOCKTAKING_DATE")
    private LocalDateTime stockTakingDate;

    @Column(name = "LOCATION_INDEX")
    private int localIndex = -1;

    @Column(name = "CARRIER")
    private boolean carrier = false;

    @Column(name = "STATION_NAME")
    private String stationName;

    @OneToMany(mappedBy = "unitLoad")
    private List<StockUnit> stockUnits = new ArrayList<>();

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public LocalDateTime getStockTakingDate() {
        return stockTakingDate;
    }

    public void setStockTakingDate(LocalDateTime stockTakingDate) {
        this.stockTakingDate = stockTakingDate;
    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }

    public int getLocalIndex() {
        return localIndex;
    }

    public void setLocalIndex(int localIndex) {
        this.localIndex = localIndex;
    }

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

    public List<StockUnit> getStockUnits() {
        return stockUnits;
    }

    public void setStockUnits(List<StockUnit> stockUnits) {
        this.stockUnits = stockUnits;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
