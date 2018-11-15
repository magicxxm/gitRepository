package wms.crud.dto;

import java.math.BigDecimal;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class InvoiceItemsDTO {

    //"itemCode": "商品编码，string (50)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (50)，必填",
    private String itemName;

    //"itemPrice": "商品单价，double (18, 2)，必填",
    private double itemPrice;

    //"itemAmount": "应收金额，double (18, 2)，必填",
    private double itemAmount;

    //"discountAmount": "折扣金额，double (18, 2)",
    private double discountAmount;

    //"planQty": "商品数量，int，必填"
    private BigDecimal planQty;

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
}
