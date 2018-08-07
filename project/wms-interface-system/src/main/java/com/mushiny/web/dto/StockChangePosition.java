package com.mushiny.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/7.
 */
public class StockChangePosition implements Serializable {

    private String lineNo;//行号

    private String itemNo;

    private String clientNo;

    private String stockState;

    private String amount;

    private String endDate;//到期日

    private String outBatchNo;//外部批次号

    private String changeClient;//更新货主

    private String changeStockState;//更新库存状态

    private String changeEndDate;//更新有效期

    private String changeOutBatch;//更新批次

    private String changeAmont;

    private String remark;//备注

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getChangeClient() {
        return changeClient;
    }

    public void setChangeClient(String changeClient) {
        this.changeClient = changeClient;
    }

    public String getChangeStockState() {
        return changeStockState;
    }

    public void setChangeStockState(String changeStockState) {
        this.changeStockState = changeStockState;
    }

    public String getChangeEndDate() {
        return changeEndDate;
    }

    public void setChangeEndDate(String changeEndDate) {
        this.changeEndDate = changeEndDate;
    }

    public String getChangeOutBatch() {
        return changeOutBatch;
    }

    public void setChangeOutBatch(String changeOutBatch) {
        this.changeOutBatch = changeOutBatch;
    }

    public String getChangeAmont() {
        return changeAmont;
    }

    public void setChangeAmont(String changeAmont) {
        this.changeAmont = changeAmont;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
