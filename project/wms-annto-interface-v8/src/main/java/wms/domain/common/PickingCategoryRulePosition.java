package wms.domain.common;


import wms.common.entity.BaseEntity;
import wms.constants.PickingOperator;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKINGCATEGORYRULEPOSITION")
public class PickingCategoryRulePosition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private String number;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int index = 0;

    @Column(name = "OPERATOR", nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingOperator operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RULE_ID")
    private PickingCategoryRule pickingCategoryRule;

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

    public PickingOperator getOperator() {
        return operator;
    }

    public void setOperator(PickingOperator operator) {
        this.operator = operator;
    }

    public PickingCategoryRule getPickingCategoryRule() {
        return pickingCategoryRule;
    }

    public void setPickingCategoryRule(PickingCategoryRule pickingCategoryRule) {
        this.pickingCategoryRule = pickingCategoryRule;
    }
}
