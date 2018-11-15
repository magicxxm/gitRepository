package wms.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 123 on 2017/12/15.
 */
public class ReceiptPositionResult implements Serializable {

    private BigDecimal amountTotal;

    private String itemId;

    private String lotNo;

    private String state;


    public ReceiptPositionResult() {
    }

    public ReceiptPositionResult(BigDecimal amountTotal, String itemId, String lotNo, String state) {
        this.amountTotal = amountTotal;
        this.itemId = itemId;
        this.lotNo = lotNo;
        this.state = state;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
