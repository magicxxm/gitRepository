package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_POD", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class Pod extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POD_INDEX", nullable = false)
    private int podIndex = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PODTYPE_ID")
    private PodType podType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    @Column(name = "YPOS")
    private int yPos;

    @Column(name = "XPOS")
    private int xPos;

    @Column(name = "SELLING_DEGREE")
    private String sellingDegree;

    @Column(name = "PLACEMARK")
    private int placeMark;

    @Column(name = "TOWARD")
    private int toWard;

    @Column(name = "STATE")
    private String state;

    @Column(name = "XPOS_TAR")
    private int xPosTar;

    @Column(name = "YPOS_TAR")
    private int yPosTar;

    @Column(name = "ADDRCODEID_TAR")
    private int addrcodeIdTar;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID",nullable = false)
    private Section section;

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

    public int getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(int podIndex) {
        this.podIndex = podIndex;
    }

    public Zone getZone() {
        return zone;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }

    public String getSellingDegree() {
        return sellingDegree;
    }

    public void setSellingDegree(String sellingDegree) {
        this.sellingDegree = sellingDegree;
    }

    public PodType getPodType() {
        return podType;
    }

    public void setPodType(PodType podType) {
        this.podType = podType;
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

    public int getAddrcodeIdTar() {
        return addrcodeIdTar;
    }

    public void setAddrcodeIdTar(int addrcodeIdTar) {
        this.addrcodeIdTar = addrcodeIdTar;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

}
