package com.mushiny.wms.application.domain;


import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_POD")
public class Pod extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SECTION_ID")
    private String sectionId;
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POD_INDEX")
    private Integer podIndex = 0;

    @Column(name = "ADDRCODEID_TAR")
    private String tarAddrcodeId;


    @Column(name = "PODTYPE_ID")
    private String podTypeId;

    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "PLACEMARK")
    private Integer placeMark;

    @Column(name = "XPOS")
    private Integer xPos;
    @Column(name = "STATE")
    private String state;
    @Column(name = "TOWARD")
    private Integer toward;

    public Integer getToward() {
        return toward;
    }

    public void setToward(Integer toward) {
        this.toward = toward;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "YPOS")
    private Integer yPos;

    @Column(name = "SELLING_DEGREE")
    private String sellingDegree;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTarAddrcodeId() {
        return tarAddrcodeId;
    }

    public void setTarAddrcodeId(String tarAddrcodeId) {
        this.tarAddrcodeId = tarAddrcodeId;
    }

    public Integer getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(Integer podIndex) {
        this.podIndex = podIndex;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(Integer placeMark) {
        this.placeMark = placeMark;
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

    public String getSellingDegree() {
        return sellingDegree;
    }

    public void setSellingDegree(String sellingDegree) {
        this.sellingDegree = sellingDegree;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
