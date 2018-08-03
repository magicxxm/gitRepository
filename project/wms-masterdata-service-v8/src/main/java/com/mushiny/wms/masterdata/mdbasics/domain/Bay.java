package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_BAY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class Bay extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AISLE")
    private String aisle;

    @Column(name = "BAY_INDEX", nullable = false)
    private int bayIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PODTYPE_ID")
    private PodType podType;

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public int getBayIndex() {
        return bayIndex;
    }

    public void setBayIndex(int bayIndex) {
        this.bayIndex = bayIndex;
    }

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

    public PodType getPodType() {
        return podType;
    }

    public void setPodType(PodType podType) {
        this.podType = podType;
    }
}
