package com.mushiny.wcs.application.utils;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/25.
 */
public class UpdateCost {
    private  String time;
    private String addressList;
    private String costValue;

    private String costType;

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getCostValue() {
        return costValue;
    }

    public void setCostValue(String costValue) {
        this.costValue = costValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }
}
