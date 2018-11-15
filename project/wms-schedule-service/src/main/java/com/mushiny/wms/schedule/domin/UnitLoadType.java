package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/4/27.
 */
@Entity
@Table(name="INV_UNITLOADTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class UnitLoadType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HEIGHT", precision = 15, scale = 2)
    private BigDecimal height;

    @Column(name = "WIDTH", precision = 15, scale = 2)
    private BigDecimal width;

    @Column(name = "DEPTH", precision = 15, scale = 2)
    private BigDecimal depth;

    @Column(name = "VOLUME", precision = 19, scale = 6)
    private BigDecimal volume;

    @Column(name = "WEIGHT", precision = 16, scale = 3)
    private BigDecimal weight;

    @Column(name = "LIFTING_CAPACITY", precision = 16, scale = 3)
    private BigDecimal liftingCapacity;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLiftingCapacity() {
        return liftingCapacity;
    }

    public void setLiftingCapacity(BigDecimal liftingCapacity) {
        this.liftingCapacity = liftingCapacity;
    }
}
