package com.mushiny.wms.schedule.domin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_PODTYPEPOSITION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "POSITION_NO", "WAREHOUSE_ID"
        })
})
public class PodTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo;

    @Column(name = "LEVEL", nullable = false)
    private int level;

    @Column(name = "NUMBER_OF_COLUMNS", nullable = false)
    private int numberOfColumns = 0;

    @Column(name = "FACE")
    private String face;

    @Column(name = "COLOR")
    private String color;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STORAGELOCATIONTYPE_ID")
    private StorageLocationType storageLocationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DROPZONE_ID")
    private DropZone dropZone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PODTYPE_ID")
    @JsonIgnore
    private PodType podType;

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public StorageLocationType getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationType storageLocationType) {
        this.storageLocationType = storageLocationType;
    }

    public DropZone getDropZone() {
        return dropZone;
    }

    public void setDropZone(DropZone dropZone) {
        this.dropZone = dropZone;
    }

    public PodType getPodType() {
        return podType;
    }

    public void setPodType(PodType podType) {
        this.podType = podType;
    }

}
