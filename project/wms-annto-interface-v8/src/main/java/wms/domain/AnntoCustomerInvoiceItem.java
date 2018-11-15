package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/15.
 */
@Entity
@Table(name="ANNTO_CUSTOMERINVOICEITEM")
public class AnntoCustomerInvoiceItem extends BaseEntity {

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "ITEM_PRICE")
    private double itemPrice;

    @Column(name = "ITEM_AMOUNT")
    private double itemAmount;

    @Column(name = "DISCOUNT_AMOUNT")
    private double discountAmount;

    @Column(name = "PLAN_QTY")
    private BigDecimal planQty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID",nullable = false)
    private AnntoCustomerOrderPosition anntoCustomerOrderPosition;

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

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public AnntoCustomerOrderPosition getAnntoCustomerOrderPosition() {
        return anntoCustomerOrderPosition;
    }

    public void setAnntoCustomerOrderPosition(AnntoCustomerOrderPosition anntoCustomerOrderPosition) {
        this.anntoCustomerOrderPosition = anntoCustomerOrderPosition;
    }
}
