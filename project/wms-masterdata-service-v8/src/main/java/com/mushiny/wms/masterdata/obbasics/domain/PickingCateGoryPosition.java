package com.mushiny.wms.masterdata.obbasics.domain;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKINGCATEGORYPOSITION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"POSITION_NO", "CLIENT_ID", "WAREHOUSE_ID"})
})
public class PickingCateGoryPosition extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private String number;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int index = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    private PickingCateGory pickingCategory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RULE_ID")
    private PickingCateGoryRule pickingCateGoryRule;

    @Column(name = "OPERATOR", nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingOperator operator;

    @Column(name = "COMP_VALUE")
    private String value;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PickingCateGory getPickingCategory() {
        return pickingCategory;
    }

    public void setPickingCategory(PickingCateGory pickingCategory) {
        this.pickingCategory = pickingCategory;
    }

    public PickingCateGoryRule getPickingCateGoryRule() {
        return pickingCateGoryRule;
    }

    public void setPickingCateGoryRule(PickingCateGoryRule pickingCateGoryRule) {
        this.pickingCateGoryRule = pickingCateGoryRule;
    }

    public PickingOperator getOperator() {
        return operator;
    }

    public void setOperator(PickingOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
