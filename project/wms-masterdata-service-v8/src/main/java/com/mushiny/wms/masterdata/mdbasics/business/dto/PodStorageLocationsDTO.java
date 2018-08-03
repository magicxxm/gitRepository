package com.mushiny.wms.masterdata.mdbasics.business.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PodStorageLocationsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String clientId;

    @NotNull
    private String zoneId;

    private int xpos;

    private int ypos;

    @NotNull
    private int fromPod = 0;

    @NotNull
    private int toPod = 0;

    @NotNull
    private String podTypeId;

    @NotNull
    private String areaId;

    private String sellingDegree;

    private int placeMark;

    private int toWard;

    private String state;

    private int xPosTar;

    private int yPosTar;

    private int addrCodeIdTar;

    private String sectionId;

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    private String description;

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

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

    public int getFromPod() {
        return fromPod;
    }

    public void setFromPod(int fromPod) {
        this.fromPod = fromPod;
    }

    public int getToPod() {
        return toPod;
    }

    public void setToPod(int toPod) {
        this.toPod = toPod;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getSellingDegree() {
        return sellingDegree;
    }

    public void setSellingDegree(String sellingDegree) {
        this.sellingDegree = sellingDegree;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }

    public int getToWard() {
        return toWard;
    }

    public void setToWard(int toWard) {
        this.toWard = toWard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getxPosTar() {
        return xPosTar;
    }

    public void setxPosTar(int xPosTar) {
        this.xPosTar = xPosTar;
    }

    public int getyPosTar() {
        return yPosTar;
    }

    public void setyPosTar(int yPosTar) {
        this.yPosTar = yPosTar;
    }

    public int getAddrCodeIdTar() {
        return addrCodeIdTar;
    }

    public void setAddrCodeIdTar(int addrCodeIdTar) {
        this.addrCodeIdTar = addrCodeIdTar;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
