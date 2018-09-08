package com.mushiny.wms.pathPlanning.utils;

import org.springframework.util.ObjectUtils;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/30.
 */
public class PathPair {
    private Integer start;
    private Integer end;
    private Integer carryingCost;
    private Integer  cost;
    private Integer newCost;
    //权重
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer driverType) {
        int costTemp;
        if(driverType==1)
        {
            costTemp=carryingCost;

        }else{
            costTemp=cost;

        }
        if(cost != -1)
        {
            weight=costTemp+( ObjectUtils.isEmpty(newCost)?0:newCost);
        }else{
            weight=-1;
        }

    }

    public PathPair(Integer start, Integer end, Integer carryingCost, Integer cost, Integer newCost) {
        this.start = start;
        this.end = end;
        this.carryingCost = carryingCost;
        this.cost = cost;
        this.newCost = newCost;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getCarryingCost() {
        return carryingCost;
    }

    public void setCarryingCost(Integer carryingCost) {
        this.carryingCost = carryingCost;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getNewCost() {
        return newCost;
    }

    public void setNewCost(Integer newCost) {
        this.newCost = newCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathPair)) return false;

        PathPair pathPair = (PathPair) o;

        if (!start.equals(pathPair.start)) return false;
        return end.equals(pathPair.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
}
