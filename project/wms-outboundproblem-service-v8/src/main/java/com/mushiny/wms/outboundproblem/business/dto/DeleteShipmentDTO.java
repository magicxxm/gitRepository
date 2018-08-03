package com.mushiny.wms.outboundproblem.business.dto;


import com.mushiny.wms.common.crud.dto.BaseDTO;

import java.math.BigDecimal;

public class DeleteShipmentDTO extends BaseDTO{
    private static final long serialVersionUID = 1L;


    private String containerName;

    private String shipmentNo;

    private String itemNo;

    private String serialNo;

    private String useNotAfter;

    private BigDecimal amount = BigDecimal.ONE ;

    private String solveKey;

    private String stationId;

    private String wallId;

    private BigDecimal amountNormal;

    public BigDecimal getAmountNormal() {
        return amountNormal;
    }

    public void setAmountNormal(BigDecimal amountNormal) {
        this.amountNormal = amountNormal;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getWallId() {
        return wallId;
    }

    public void setWallId(String wallId) {
        this.wallId = wallId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(String useNotAfter) {
        if (!useNotAfter.isEmpty())
            useNotAfter = useNotAfter + " 00:00:00";
        this.useNotAfter = useNotAfter;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSolveKey() {
        return solveKey;
    }

    public void setSolveKey(String solveKey) {
        this.solveKey = solveKey;
    }
}
