package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBINCUSTOMERSHIPMENT")
public class RebinCustomerShipment extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINREQUEST_ID")
    private RebinRequest rebinRequest;

    @Column(name = "PICKINGORDER_NO", nullable = true)
    private String pickingOrderNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMERSHIPMENT_ID")
    private CustomerShipment customerShipment;

    @Column(name = "REBINWALL_INDEX", nullable = false)
    private int rebinWallIndex = 0;

    @Column(name = "REBINCELL_INDEX", nullable = false)
    private int rebinCellIndex = 0;

    @Column(name = "REBINCELL_NAME", nullable = false)
    private String rebinCellName;

    public RebinRequest getRebinRequest() {
        return rebinRequest;
    }

    public void setRebinRequest(RebinRequest rebinRequest) {
        this.rebinRequest = rebinRequest;
    }

    public String getPickingOrderNumber() {
        return pickingOrderNumber;
    }

    public void setPickingOrderNumber(String pickingOrderNumber) {
        this.pickingOrderNumber = pickingOrderNumber;
    }

    public CustomerShipment getCustomerShipment() {
        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }

    public int getRebinWallIndex() {
        return rebinWallIndex;
    }

    public void setRebinWallIndex(int rebinWallIndex) {
        this.rebinWallIndex = rebinWallIndex;
    }

    public int getRebinCellIndex() {
        return rebinCellIndex;
    }

    public void setRebinCellIndex(int rebinCellIndex) {
        this.rebinCellIndex = rebinCellIndex;
    }

    public String getRebinCellName() {
        return rebinCellName;
    }

    public void setRebinCellName(String rebinCellName) {
        this.rebinCellName = rebinCellName;
    }

}
