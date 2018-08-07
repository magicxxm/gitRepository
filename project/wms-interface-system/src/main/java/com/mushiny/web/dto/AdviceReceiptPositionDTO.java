package com.mushiny.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/2.
 */
public class AdviceReceiptPositionDTO implements Serializable {

    private String notifiedAmount;//应收数量

    private String itemNo;//商品唯一编码

    private String clientNo;

    private String stockState;

    private String productDate;//生产日期

    private String endDate;//到期日期

    private String outBatchNo;//外部批次号

    public String getNotifiedAmount() {
        return notifiedAmount;
    }

    public void setNotifiedAmount(String notifiedAmount) {
        this.notifiedAmount = notifiedAmount;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOutBatchNo() {
        return outBatchNo;
    }

    public void setOutBatchNo(String outBatchNo) {
        this.outBatchNo = outBatchNo;
    }
}
