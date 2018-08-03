package com.mushiny.wms.report.query.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

// fud 与 遗留数据
public class FudDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemDataId;

    private String skuNo;

    private String skuId;

    private String skuName;

    private String clientName;

    private String containerName;

    private BigDecimal amount = BigDecimal.ZERO;

    private String activityCode;

    private String state;

    private Timestamp modifiedDate;

    private String overTime;    // 影响客户订单时间

    private String totalTime;  // 累计时间


    public FudDTO() {
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }


}
