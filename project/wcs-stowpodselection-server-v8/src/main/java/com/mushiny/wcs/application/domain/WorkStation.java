package com.mushiny.wcs.application.domain;


import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MD_WORKSTATION")
public class WorkStation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "XPOS")
    private Integer xPos;

    @Column(name = "YPOS")
    private Integer yPos;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TYPE_ID")
    private String typeId;

    @Column(name = "PICKPACKWALL_ID")
    private String pickPackWallId;

    @Column(name = "FIXED_SCANNER")
    private boolean fixedScanner;

    @Column(name = "WORKING_FACE_ORIENTATION")
    private Integer toward;

    @Column(name = "BUFFERPOINT")
    private Integer bufferPoind;

    @Column(name = "PLACEMARK")
    private Integer placeMark;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "ISCALLPOD")
    private boolean isCallPod;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public boolean isFixedScanner() {
        return fixedScanner;
    }

    public void setFixedScanner(boolean fixedScanner) {
        this.fixedScanner = fixedScanner;
    }

    public Integer getToward() {
        return toward;
    }

    public void setToward(Integer toward) {
        this.toward = toward;
    }

    public Integer getBufferPoind() {
        return bufferPoind;
    }

    public void setBufferPoind(Integer bufferPoind) {
        this.bufferPoind = bufferPoind;
    }

    public Integer getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(Integer placeMark) {
        this.placeMark = placeMark;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
