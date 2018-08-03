package com.mushiny.wms.masterdata.mdbasics.business.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BayStorageLocationsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String clientId;

    @NotNull
    private String zoneId;

    @NotNull
    private int fromAisle = 0;

    @NotNull
    private int toAisle = 0;

    @NotNull
    private int fromBay = 0;

    @NotNull
    private int toBay = 0;

    @NotNull
    private int index = 0;

    @NotNull
    private String podTypeId;

    @NotNull
    private String areaId;

    private String additionalContent;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public int getFromAisle() {
        return fromAisle;
    }

    public void setFromAisle(int fromAisle) {
        this.fromAisle = fromAisle;
    }

    public int getToAisle() {
        return toAisle;
    }

    public void setToAisle(int toAisle) {
        this.toAisle = toAisle;
    }

    public int getFromBay() {
        return fromBay;
    }

    public void setFromBay(int fromBay) {
        this.fromBay = fromBay;
    }

    public int getToBay() {
        return toBay;
    }

    public void setToBay(int toBay) {
        this.toBay = toBay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAdditionalContent() {
        return additionalContent;
    }

    public void setAdditionalContent(String additionalContent) {
        this.additionalContent = additionalContent;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }
}
