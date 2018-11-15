package com.mushiny.wms.schedule.domin;

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

    @Column(name = "PLACEMARK")
    private String placeMark;

    @Column(name = "SELLING_DEGREE")
    private String sellingDegree;

    @Column(name = "SECTION_ID")
    private String sectionId;

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

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(String placeMark) {
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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
