package com.mushiny.wcs.application.domain;


import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "PQA_ENROUTEPOD")
public class PqaEnroutePod extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "WORKLOAD")
    private BigDecimal workLoad;
    @Column(name = "STATION_ID", nullable = false)
    private String stationId;
    @Column(name = "POD_ID")
    private String podId;
    @Column(name = "ROUTETYPE")
    private String routeType;

    public BigDecimal getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(BigDecimal workLoad) {
        this.workLoad = workLoad;
    }

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

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }


}
