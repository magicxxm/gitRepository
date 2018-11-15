package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/5/22.
 */
@Entity
@Table(name="OB_CUSTOMERSHIPMENTRECORD")
public class CustomerShipmentRecord extends BaseClientAssignedEntity {

    @Column(name="SHIPMENT_ID")
    private String shipmentId;

    @Column(name="STATE")
    private int state;

    @Column(name="STATE_NAME")
    private String stateName;

    @Column(name="STATION_NAME")
    private String stationName;

    @JoinColumn(name="OPERATOR_ID")
    @ManyToOne
    private User operator;

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

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

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }
}
