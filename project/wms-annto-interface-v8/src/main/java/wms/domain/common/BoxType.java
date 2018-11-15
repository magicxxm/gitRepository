package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name="OB_BOXTYPE")
public class BoxType extends BaseClientAssignedEntity {

    @Column(name="TYPE_GROUP",nullable = false)
    private String typeGroup;

    @Column(name="NAME",nullable = false)
    private String name;

//    @Column(name = "BOX_CODE")
//    private String boxCode;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="HEIGHT")
    private BigDecimal height;

    @Column(name="WIDTH")
    private BigDecimal width;

    @Column(name="DEPTH")
    private BigDecimal depth;

    @Column(name="THICKNESS")
    private BigDecimal thickness;

    @Column(name="VOLUME")
    private BigDecimal volume;

    @Column(name="WEIGHT")
    private BigDecimal weight;

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getBoxCode() {
//        return boxCode;
//    }
//
//    public void setBoxCode(String boxCode) {
//        this.boxCode = boxCode;
//    }

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

    public BigDecimal getThickness() {
        return thickness;
    }

    public void setThickness(BigDecimal thickness) {
        this.thickness = thickness;
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
}
