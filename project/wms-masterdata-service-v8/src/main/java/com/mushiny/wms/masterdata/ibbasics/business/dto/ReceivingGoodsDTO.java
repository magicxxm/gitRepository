package com.mushiny.wms.masterdata.ibbasics.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceivingGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String receivingContainerId;

    private String itemDataId;

    // DN
    private String adviceId;

    // 商品总数量
    private BigDecimal amount;

    // 批次到期日期
    private LocalDate useNotAfter;

    // SN唯一条码
    private String serialNo;

    // 收货类型
    private String receivingType;

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public String getReceivingContainerId() {
        return receivingContainerId;
    }

    public void setReceivingContainerId(String receivingContainerId) {
        this.receivingContainerId = receivingContainerId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(LocalDate useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getReceivingType() {
        return receivingType;
    }

    public void setReceivingType(String receivingType) {
        this.receivingType = receivingType;
    }
}
