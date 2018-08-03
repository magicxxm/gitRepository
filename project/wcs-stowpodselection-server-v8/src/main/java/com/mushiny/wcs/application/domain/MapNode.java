package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WD_NODE")
public class MapNode extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Column(name = "NAME")

    private String name;

    @Column(name = "XPOSITION")
    private Integer xPosition;

    @Column(name = "YPOSITION")
    private Integer yPosition;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "BLOCKED")
    private boolean blocked;

    @Column(name = "ADDRESSCODEID")
    private Integer addressCodeId;

    @Column(name = "MAP_ID")
    private String mapId;
    @Column(name = "CLASSGROUP")
    private String classGroup;
    @Column(name = "CLASSVALUE")
    private Integer classValue;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }

    public Integer getClassValue() {
        return classValue;
    }

    public void setClassValue(Integer classValue) {
        this.classValue = classValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(int addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }


}
