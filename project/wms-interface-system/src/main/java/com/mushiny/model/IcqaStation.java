package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/27.
 */
@Entity
@Table(name = "ICQA_STOCKTAKINGSTATION")
public class IcqaStation extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "WORKSTATION_ID")
    private String workStationId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkStationId() {
        return workStationId;
    }

    public void setWorkStationId(String workStationId) {
        this.workStationId = workStationId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
