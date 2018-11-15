package wms.domain.common;

import wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Entity
@Table(name = "ICQA_STOCKTAKINGRULE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME"
        })
})
public class StocktakingRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "OPERATOR", nullable = false)
    private String operator;

    @Column(name = "DECISION_KEY", nullable = false)
    private String decisionKey;

    @Column(name = "COMPARISON_TYPE", nullable = false)
    private String comparisonType;

    @Column(name = "COMP_KEY")
    private String compKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDecisionKey() {
        return decisionKey;
    }

    public void setDecisionKey(String decisionKey) {
        this.decisionKey = decisionKey;
    }

    public String getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
    }
}


