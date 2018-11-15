package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/26.
 */
@Entity
@Table(name = "OB_PICKINGUNITLOAD")
public class PickingUnitLoad extends BaseClientAssignedEntity {
    @Column(name = "CUSTOMERSHIPMENT_NO")
    private String customerShipmentNo;

    @Column(name = "POSITION_INDEX", nullable = false)
    private int positionIndex = 0;

    @ManyToOne
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @Column(name = "STATE")
    private int state = 0;

    @ManyToOne
    @JoinColumn(name = "UNITLOAD_ID")
    private UnitLoad unitLoad;

/*    @ManyToOne
    @JoinColumn(name = "PICKSTATIONPOSITION_ID")
    private PickStationPosition pickStationPosition;

    @ManyToOne()
    @JoinColumn(name = "PICKSTATION_ID")
    private PickStation pickStation;*/

    @Column(name = "COMPLETE")
    private  boolean isComplete = false;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    public PickingUnitLoad() {
    }

    public String getCustomerShipmentNo() {
        return customerShipmentNo;
    }

    public void setCustomerShipmentNo(String customerShipmentNo) {
        this.customerShipmentNo = customerShipmentNo;
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

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }
}
