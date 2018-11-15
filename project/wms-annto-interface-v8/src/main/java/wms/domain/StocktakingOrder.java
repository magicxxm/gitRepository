package wms.domain;

import wms.common.entity.BaseWarehouseAssignedEntity;
import wms.domain.common.StocktakingRule;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by PC-4 on 2017/8/3.
 */
@Entity
@Table(name = "ICQA_STOCKTAKINGORDER")
public class StocktakingOrder extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "AREA_NAME")
    private String areaName;

    @Column(name = "COUNTING_DATE")
    private LocalDateTime countingDate;

    @Column(name = "LOCATION_NAME")
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "STOCKTAKINGRULE_ID")
    private StocktakingRule stocktakingRule;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "TIMES")
    private Integer times;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "UNITLOAD_LABEL")
    private String unitLoadLabel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCKTAKING_ID")
    private Stocktaking stocktaking;

    @Column(name = "PARAMETER", nullable = false)
    private String parameter;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public LocalDateTime getCountingDate() {
        return countingDate;
    }

    public void setCountingDate(LocalDateTime countingDate) {
        this.countingDate = countingDate;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public StocktakingRule getStocktakingRule() {
        return stocktakingRule;
    }

    public void setStocktakingRule(StocktakingRule stocktakingRule) {
        this.stocktakingRule = stocktakingRule;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUnitLoadLabel() {
        return unitLoadLabel;
    }

    public void setUnitLoadLabel(String unitLoadLabel) {
        this.unitLoadLabel = unitLoadLabel;
    }

    public Stocktaking getStocktaking() {
        return stocktaking;
    }

    public void setStocktaking(Stocktaking stocktaking) {
        this.stocktaking = stocktaking;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

}
