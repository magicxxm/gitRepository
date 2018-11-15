package wms.domain.common;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/28.
 */
@Entity
@Table(name = "OB_PICKPACKCELL")
public class PickPackCell extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TYPE_ID", nullable = false)
    private String pickPackCellTypeId;

    @Column(name = "XPOS")
    private int xPos;

    @Column(name = "YPOS")
    private int yPos;

    @Column(name = "ZPOS")
    private int zPos;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "FIELD_INDEX")
    private int fieldIndex;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex;

    @Column(name = "LABEL_COLOR", nullable = false)
    private String labelColor;

    @Column(name = "PICKPACKWALL_ID", nullable = false)
    private String pickPackWallId;

    @Column(name = "DIGITALLABEL1_ID")
    private String digitalabel1Id;

    @Column(name = "DIGITALLABEL2_ID")
    private String digitalabel2Id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickPackCellTypeId() {
        return pickPackCellTypeId;
    }

    public void setPickPackCellTypeId(String pickPackCellTypeId) {
        this.pickPackCellTypeId = pickPackCellTypeId;
    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getzPos() {
        return zPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getDigitalabel1Id() {
        return digitalabel1Id;
    }

    public void setDigitalabel1Id(String digitalabel1Id) {
        this.digitalabel1Id = digitalabel1Id;
    }

    public String getDigitalabel2Id() {
        return digitalabel2Id;
    }

    public void setDigitalabel2Id(String digitalabel2Id) {
        this.digitalabel2Id = digitalabel2Id;
    }
}
