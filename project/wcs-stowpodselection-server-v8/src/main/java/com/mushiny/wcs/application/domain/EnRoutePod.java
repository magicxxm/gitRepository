package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "IB_ENROUTEPOD")
public class EnRoutePod extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "STOW_STATION_ID")
    private String stowStationId;

    @Column(name = "RECEIVE_STATION_ID")
    private String receiveStationId;

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

    public String getReceiveStationId() {
        return receiveStationId;
    }

    public void setReceiveStationId(String receiveStationId) {
        this.receiveStationId = receiveStationId;
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
