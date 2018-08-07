package com.mushiny.repository;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/29.
 */
@Entity
@Table(name = "IB_ENROUTEPOD")
public class IBEnroutePod extends BaseEntity {

    @Column(name = "STOW_STATION_ID")
    private String stowStationId;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getStowStationId() {
        return stowStationId;
    }

    public void setStowStationId(String stowStationId) {
        this.stowStationId = stowStationId;
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
