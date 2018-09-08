package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;
import org.springframework.util.ObjectUtils;

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

    @Column(name = "STATION_ID")
    private String stationId;

    @Column(name = "CLASSGROUP")
    private String classGroup;
    @Column(name = "TURNAREA_ID")
    private String turnAreaId;

    @Column(name = "CHARGER_ID")
    private String chargerId;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "MAP_ID")
    private String mapId;

    @Column(name = "AGVTYPE")
    private Integer agvType = 0;

    @Column(name = "CLASSVALUE")
    private Integer classValue = -1;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getTurnAreaId() {
        return turnAreaId;
    }

    public void setTurnAreaId(String turnAreaId) {
        this.turnAreaId = turnAreaId;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
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

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public int getAgvType() {
        if (ObjectUtils.isEmpty(agvType))
            agvType = -1;
        return agvType;
    }

    public void setAgvType(int agvType) {
        this.agvType = agvType;
    }

    public int getClassValue() {
        if (ObjectUtils.isEmpty(classValue))
            classValue = 2;
        return classValue;
    }

    public void setClassValue(int classValue) {
        this.classValue = classValue;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
