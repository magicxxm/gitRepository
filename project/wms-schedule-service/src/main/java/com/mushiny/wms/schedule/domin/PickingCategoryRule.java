package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.schedule.common.PickingComparisonType;
import com.mushiny.wms.schedule.common.PickingRuleKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGCATEGORYRULE")
public class PickingCategoryRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "RULE_KEY", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingRuleKey key;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "COMPARISON_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingComparisonType comparisonType;

    @OneToMany(mappedBy = "pickingCategoryRule")
    @OrderBy("index ASC")
    private List<PickingCategoryRulePosition> positions = new ArrayList<>();

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

    public List<PickingCategoryRulePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingCategoryRulePosition> positions) {
        this.positions = positions;
    }
}
