package com.mushiny.model;


import com.mushiny.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/30.
 */
@Entity
@Table(name = "ICQA_SYSTEM_STOCKTAKING")
public class SystemStocktaking extends BaseWarehouseAssignedEntity {

    @Column(name = "STOCK_NO")
    private String stockNo;

    @Column(name = "STOCK_TYPE")
    private int stockType;

    @Column(name = "STATE")
    private int state = 90;

    @Column(name = "REMARK")
    private String remark;

    @OrderBy("itemNo")
    @OneToMany(mappedBy = "systemStocktaking",cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<SystemStocktakingPosition> positions = new ArrayList<>();

    public void addPosition(SystemStocktakingPosition position) {
        getPositions().add(position);
        position.setSystemStocktaking(this);
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public int getStockType() {
        return stockType;
    }

    public void setStockType(int stockType) {
        this.stockType = stockType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SystemStocktakingPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<SystemStocktakingPosition> positions) {
        this.positions = positions;
    }
}
