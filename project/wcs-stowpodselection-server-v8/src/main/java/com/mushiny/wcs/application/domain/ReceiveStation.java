package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_RECEIVESTATION")
public class ReceiveStation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OPERATOR_ID")
    private String operatorId;

    @Column(name = "TYPE_ID")
    private String typeId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @ManyToOne
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "IB_RECEIVESTATIONBINTYPE",
            joinColumns = @JoinColumn(name = "RECEIVESTATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "STORAGELOCATIONTYPE_ID"))
    private List<StorageLocationType> storageLocationTypes = new ArrayList<>();

    public List<StorageLocationType> getStorageLocationTypes() {
        return storageLocationTypes;
    }

    public void setStorageLocationTypes(List<StorageLocationType> storageLocationTypes) {
        this.storageLocationTypes = storageLocationTypes;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
