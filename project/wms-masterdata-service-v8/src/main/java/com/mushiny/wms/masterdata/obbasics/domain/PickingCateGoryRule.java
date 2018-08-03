package com.mushiny.wms.masterdata.obbasics.domain;


import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.constants.PickingComparisonType;
import com.mushiny.wms.masterdata.obbasics.constants.PickingRuleKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGCATEGORYRULE")
public class PickingCateGoryRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "RULE_KEY", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingRuleKey key;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "COMPARISON_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingComparisonType comparisonType;

    @OneToMany(mappedBy = "pickingCateGoryRule")
    @OrderBy("index ASC")
    private List<PickingCateGoryRulePosition> positions = new ArrayList<>();

    public PickingRuleKey getKey() {
        return key;
    }

    public void setKey(PickingRuleKey key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PickingComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(PickingComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public List<PickingCateGoryRulePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingCateGoryRulePosition> positions) {
        this.positions = positions;
    }
}
