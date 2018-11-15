package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/4/28.
 */
@Entity
@Table(name = "OB_PICKPACKCELLTYPE")
public class PickPackCellType extends BaseWarehouseAssignedEntity {

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
    private BigDecimal LiftingCapacity;

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
        return LiftingCapacity;
    }

    public void setLiftingCapacity(BigDecimal liftingCapacity) {
        LiftingCapacity = liftingCapacity;
    }
}
