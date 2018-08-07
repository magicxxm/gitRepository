package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/3/29.
 */
@Entity
@Table(name = "PQA_ENROUTEPOD")
public class PQAEnroutePod extends BaseEntity{

    @Column(name = "WORKLOAD")
    private BigDecimal workLoad;

    @Column(name = "STATION_ID")
    private String stationId;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouesId;

    @Column(name = "ROUTETYPE")
    private String workStationTypeId;

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

    public String getWarehouesId() {
        return warehouesId;
    }

    public void setWarehouesId(String warehouesId) {
        this.warehouesId = warehouesId;
    }

    public String getWorkStationTypeId() {
        return workStationTypeId;
    }

    public void setWorkStationTypeId(String workStationTypeId) {
        this.workStationTypeId = workStationTypeId;
    }
}
