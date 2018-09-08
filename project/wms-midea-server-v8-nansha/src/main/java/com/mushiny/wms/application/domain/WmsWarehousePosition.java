package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/7/11.
 */
@Entity
@Table(name = "WMS_WAREHOUSE_POSITION")
public class WmsWarehousePosition extends BaseEntity {
    @Column(name = "ADDRESSCODEID")
    private String addresscodeid;
    @Column(name = "NODE_STATE")
    private String nodeState;
    @Column(name = "ORDER_CODE")
    private String orderCode;
    @Column(name = "MITEM_CODE")
    private String mitemCode;
    @Column(name = "AMOUNT")
    private String amount;
    @Column(name = "POD_INDEX")
    private String podIndex;
    @Column(name = "POD_STATU")
    private String podStatu;
    @Column(name = "ROBOT_ID")
    private String robotId;

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }


    public String getAddresscodeid() {
        return addresscodeid;
    }

    public void setAddresscodeid(String addresscodeid) {
        this.addresscodeid = addresscodeid;
    }


    public String getNodeState() {
        return nodeState;
    }

    public void setNodeState(String nodeState) {
        this.nodeState = nodeState;
    }


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    public String getMitemCode() {
        return mitemCode;
    }

    public void setMitemCode(String mitemCode) {
        this.mitemCode = mitemCode;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(String podIndex) {
        this.podIndex = podIndex;
    }


    public String getPodStatu() {
        return podStatu;
    }

    public void setPodStatu(String podStatu) {
        this.podStatu = podStatu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WmsWarehousePosition that = (WmsWarehousePosition) o;

        if (addresscodeid != null ? !addresscodeid.equals(that.addresscodeid) : that.addresscodeid != null)
            return false;
        if (nodeState != null ? !nodeState.equals(that.nodeState) : that.nodeState != null) return false;
        if (orderCode != null ? !orderCode.equals(that.orderCode) : that.orderCode != null) return false;
        if (mitemCode != null ? !mitemCode.equals(that.mitemCode) : that.mitemCode != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (podIndex != null ? !podIndex.equals(that.podIndex) : that.podIndex != null) return false;
        return robotId != null ? robotId.equals(that.robotId) : that.robotId == null;
    }
}
