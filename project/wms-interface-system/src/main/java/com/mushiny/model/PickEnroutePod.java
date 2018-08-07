package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/29.
 */
@Entity
@Table(name =" OB_ENROUTEPOD")
public class PickEnroutePod extends BaseEntity {

    @Column(name = "STATION_ID")
    private String stationId;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
