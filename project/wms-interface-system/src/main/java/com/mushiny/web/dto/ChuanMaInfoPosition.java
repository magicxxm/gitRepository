package com.mushiny.web.dto;

/**
 * Created by 123 on 2018/2/23.
 */
public class ChuanMaInfoPosition {
    private String sequenceNo;//串码号

    private String itemNo;

    private String warehouseNo;

    private String clientNo;

    private String state;//串码状态

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ChuanMaInfo = {sequenceNo = "+getSequenceNo()+",,itemNo = "
                +getItemNo()+",,clientNo = "+getClientNo()+",,state = "+getState()+"}";
    }
}
