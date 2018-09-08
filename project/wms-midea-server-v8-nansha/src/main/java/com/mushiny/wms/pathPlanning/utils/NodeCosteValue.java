package com.mushiny.wms.pathPlanning.utils;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/27.
 */
public class NodeCosteValue {
    private String costType;
    private Integer outNode;
    private Integer costValue;
    private Integer inNode;

    public Integer getInNode() {
        return inNode;
    }

    public void setInNode(Integer inNode) {
        this.inNode = inNode;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Integer getOutNode() {
        return outNode;
    }

    public void setOutNode(Integer outNode) {
        this.outNode = outNode;
    }

    public Integer getCostValue() {
        return costValue;
    }

    public void setCostValue(Integer costValue) {
        this.costValue = costValue;
    }
}
