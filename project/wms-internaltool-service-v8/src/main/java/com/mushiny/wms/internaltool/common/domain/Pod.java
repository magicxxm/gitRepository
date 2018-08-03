package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "MD_POD", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class Pod extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POD_INDEX")
    private int podIndex;

    @Column(name = "PLACEMARK")
    private int placeMark;

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

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }
}
