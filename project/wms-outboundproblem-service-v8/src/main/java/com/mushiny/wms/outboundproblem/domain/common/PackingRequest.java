package com.mushiny.wms.outboundproblem.domain.common;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="OB_PACKINGREQUEST")
public class PackingRequest extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name="PACKING_NO",nullable = false)
    private String packingNo;

    @Column(name="STATE",nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name="CUSTOMERSHIPMENT_ID",nullable = false)
    private CustomerShipment customerShipment;

    @ManyToOne
    @JoinColumn(name="FROMUNITLOAD_ID")
    private UnitLoad fromUnitLoad;

    @ManyToOne
    @JoinColumn(name="TOUNITLOAD_ID")
    private UnitLoad toUnitLoad;

    @ManyToOne
    @JoinColumn(name="OPERATOR_ID")
    private User operator;

    @Column(name="WEIGHT")
    private BigDecimal weight;

    public String getPackingNo() {
        return packingNo;
    }

    public void setPackingNo(String packingNo) {
        this.packingNo = packingNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CustomerShipment getCustomerShipment() {
        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }

    public UnitLoad getFromUnitLoad() {
        return fromUnitLoad;
    }

    public void setFromUnitLoad(UnitLoad fromUnitLoad) {
        this.fromUnitLoad = fromUnitLoad;
    }

    public UnitLoad getToUnitLoad() {
        return toUnitLoad;
    }

    public void setToUnitLoad(UnitLoad toUnitLoad) {
        this.toUnitLoad = toUnitLoad;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
