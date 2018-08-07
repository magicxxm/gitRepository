package com.mushiny.model;

import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_STORAGELOCATION")
public class StorageLocation extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FACE")
    private String face;

    @Column(name = "ORDER_INDEX")
    private int orderIndex = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private StorageLocationType storageLocationType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public StorageLocationType getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationType storageLocationType) {
        this.storageLocationType = storageLocationType;
    }
}
