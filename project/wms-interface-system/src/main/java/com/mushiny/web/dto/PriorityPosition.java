package com.mushiny.web.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2018/3/1.
 */
public class PriorityPosition implements Serializable {

    private String warehouseNo;

    private String orderNo;

    private String priority;

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
