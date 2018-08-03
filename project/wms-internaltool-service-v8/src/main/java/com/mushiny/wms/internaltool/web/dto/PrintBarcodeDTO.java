package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;

public class PrintBarcodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String stationId;

    private String value;

    private int amount = 0;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
