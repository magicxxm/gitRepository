package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKPACKCELLTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class PickPackCellType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HEIGHT")
    private BigDecimal height;

    @Column(name = "WIDTH")
    private BigDecimal width;

    @Column(name = "DEPTH")
    private BigDecimal depth;

    @Column(name = "VOLUME")
    private BigDecimal volume;

    @Column(name = "LIFTING_CAPACITY")
    private BigDecimal liftingCapacity;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "OB_PICKPACKCELLTYPE_OB_BOXTYPE",
            joinColumns = @JoinColumn(name = "PICKPACKCELLTYPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOXTYPE_ID"))
    private List<BoxType> boxType = new ArrayList<>();

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

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLiftingCapacity() {
        return liftingCapacity;
    }

    public void setLiftingCapacity(BigDecimal liftingCapacity) {
        this.liftingCapacity = liftingCapacity;
    }

    public List<BoxType> getBoxType() {
        return boxType;
    }

    public void setBoxType(List<BoxType> boxType) {
        this.boxType = boxType;
    }
}
