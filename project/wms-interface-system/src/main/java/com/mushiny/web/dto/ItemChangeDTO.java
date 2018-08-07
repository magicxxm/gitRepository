package com.mushiny.web.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2018/2/6.
 */
public class ItemChangeDTO implements Serializable {

    private String serialNumber;

    private String itemNo;

    private String warehouseNo;

    private String clientNo;

    private String snState;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getSnState() {
        return snState;
    }

    public void setSnState(String snState) {
        this.snState = snState;
    }
}
