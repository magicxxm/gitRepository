package com.mushiny.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/5.
 */
public class CustomerShipmentDTO implements Serializable{

    private String deliveryDate;

    private String sortCode;

    private String orderNo;

    private String type;//订单类型

    private String fsType; //任务下发类型

    private String containerType;//容器类型

    private String ztbpri;//订单优先级

    private String  warehouseNo;

    private List<CustomerShipmentPositionDTO> positions = new ArrayList<>();

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFsType() {
        return fsType;
    }

    public void setFsType(String fsType) {
        this.fsType = fsType;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getZtbpri() {
        return ztbpri;
    }

    public void setZtbpri(String ztbpri) {
        this.ztbpri = ztbpri;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public List<CustomerShipmentPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<CustomerShipmentPositionDTO> positions) {
        this.positions = positions;
    }
}
