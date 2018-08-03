package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name="OB_CUSTOMERSHIPMENTRECORD")
public class CustomerShipmentRecord extends BaseClientAssignedEntity {
    private static final long serialVersionUID=1L;

    @Column(name="SHIPMENT_ID")
    private String customerShipmentId;

    @Column(name="STATE")
    private int state;

    @Column(name="STATE_NAME")
    private String stateName;

    @Column(name="STATION_NAME")
    private String stationName;

    @Column(name="OPERATOR_ID")
    private String operatorId;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCustomerShipmentId() {
        return customerShipmentId;
    }

    public void setCustomerShipmentId(String customerShipmentId) {
        this.customerShipmentId = customerShipmentId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
