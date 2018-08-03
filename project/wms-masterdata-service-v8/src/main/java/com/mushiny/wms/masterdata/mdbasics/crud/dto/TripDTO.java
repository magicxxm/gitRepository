package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import java.io.Serializable;

public class TripDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String createDate;

    private String modifiedDate;

    private String tripState;

    private String tripType;

    private String podName="";

    private String stationName="";

    private String driveId="";

    private String userName;

    //充电桩
    private String charger="";

    //调度单完成时间间隔
    private long time;

    private int itemDataAmount=0;

    private int storageLocationAmount=0;

    private int faceAmount=0;

    private String logicName="";

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getItemDataAmount() {
        return itemDataAmount;
    }

    public void setItemDataAmount(int itemDataAmount) {
        this.itemDataAmount = itemDataAmount;
    }

    public int getStorageLocationAmount() {
        return storageLocationAmount;
    }

    public void setStorageLocationAmount(int storageLocationAmount) {
        this.storageLocationAmount = storageLocationAmount;
    }

    public int getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(int faceAmount) {
        this.faceAmount = faceAmount;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }
}
