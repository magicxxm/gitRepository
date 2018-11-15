package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.schedule.common.CustomerShipmentState;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBINUNITLOAD")
public class RebinUnitLoad extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINREQUEST_ID")
    private RebinRequest rebinRequest;

    @Column(name = "CUSTOMERSHIPMENT_NO", nullable = true)
    private String customerShipmentNumber;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "UNITLOAD_ID")
    private UnitLoad unitLoad;

    @Column(name = "POSITION_INDEX", nullable = false)
    private int positionIndex = 0;

    @Column(name = "STATE", nullable = false)
    private int state = CustomerShipmentState.RAW;

    public RebinRequest getRebinRequest() {
        return rebinRequest;
    }

    public void setRebinRequest(RebinRequest rebinRequest) {
        this.rebinRequest = rebinRequest;
    }

    public String getCustomerShipmentNumber() {
        return customerShipmentNumber;
    }

    public void setCustomerShipmentNumber(String customerShipmentNumber) {
        this.customerShipmentNumber = customerShipmentNumber;
    }

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
