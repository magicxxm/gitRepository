package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/16.
 */
@Entity
@Table(name = "ANNTO_CUSTOMERORDERITEMS")
public class AnntoCustomerOrderItems extends BaseEntity {

    @Column(name = "LINE_NO")
    private String lineNo;

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "INVENTORY_TYPE")
    private String inventoryType;

    @Column(name = "PLAN_QTY")
    private BigDecimal planQty;

    @Column(name = "UNIT")
    private String unit = "EA";

    @Column(name = "KIT_FLAG")
    private String kitFlag;

    @Column(name = "REMARK")
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private AnntoCustomerOrder anntoCustomerOrder;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKitFlag() {
        return kitFlag;
    }

    public void setKitFlag(String kitFlag) {
        this.kitFlag = kitFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AnntoCustomerOrder getAnntoCustomerOrder() {
        return anntoCustomerOrder;
    }

    public void setAnntoCustomerOrder(AnntoCustomerOrder anntoCustomerOrder) {
        this.anntoCustomerOrder = anntoCustomerOrder;
    }
}
