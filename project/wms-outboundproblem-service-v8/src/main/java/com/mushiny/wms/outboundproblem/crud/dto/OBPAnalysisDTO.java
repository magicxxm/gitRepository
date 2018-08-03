package com.mushiny.wms.outboundproblem.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBPAnalysisDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String invId;

    private String storageLocation;

    private BigDecimal amount;

    private String batchId;

    private String stationName;

    private String client;

    private String clientName;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRebinUser() {
        return rebinUser;
    }

    public void setRebinUser(String rebinUser) {
        this.rebinUser = rebinUser;
    }

    public LocalDateTime getRebinDate() {
        return rebinDate;
    }

    public void setRebinDate(LocalDateTime rebinDate) {
        this.rebinDate = rebinDate;
    }

    public String getItemdataId() {
        return itemdataId;
    }

    public void setItemdataId(String itemdataId) {
        this.itemdataId = itemdataId;
    }

    private String rebinUser;

    private LocalDateTime rebinDate;

    private String itemdataId;

    public OBPAnalysisDTO() {

    }


    @Override
    public String toString() {
        return "OBPAnalysisDTO{" +
                "storageLocation='" + storageLocation + '\'' +
                ", amount='" + amount + '\'' +
                ", batchId=" + batchId + '\'' +
                ", stationName=" + stationName + '\'' +
                ", client=" + client +'\'' +
                ", clientName=" + clientName +'\'' +
                ", rebinUser=" + rebinUser +'\'' +
                ", rebinDate=" + rebinDate +'\'' +
                ", itemdataId=" + itemdataId +'\'' +
                ", clientName=" + clientName +'\'' +
                '}';
        }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }
}