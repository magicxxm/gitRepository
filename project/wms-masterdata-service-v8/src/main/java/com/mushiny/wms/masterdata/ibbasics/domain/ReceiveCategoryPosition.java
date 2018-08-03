package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "IB_RECEIVECATEGORYPOSITION")
public class ReceiveCategoryPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo = 0;

    @Column(name = "OPERATOR", nullable = false)
    private String operator;

    @Column(name = "COMP_KEY")
    private String compKey;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    private ReceiveCategory receivingCategory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RULE_ID")
    private ReceiveCategoryRule receivingCategoryRule;

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
    }

    public ReceiveCategory getReceivingCategory() {
        return receivingCategory;
    }

    public void setReceivingCategory(ReceiveCategory receivingCategory) {
        this.receivingCategory = receivingCategory;
    }

    public ReceiveCategoryRule getReceivingCategoryRule() {
        return receivingCategoryRule;
    }

    public void setReceivingCategoryRule(ReceiveCategoryRule receivingCategoryRule) {
        this.receivingCategoryRule = receivingCategoryRule;
    }
}
