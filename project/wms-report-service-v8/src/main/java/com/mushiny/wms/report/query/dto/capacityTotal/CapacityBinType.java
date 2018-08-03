package com.mushiny.wms.report.query.dto.capacityTotal;

import java.io.Serializable;

public class CapacityBinType implements Serializable {
    private static final long serialVersionUID = 1L;

    private String binType;

    private String binTypeUtilization;

    public CapacityBinType() {
    }

    public CapacityBinType(String binType, String binTypeUtilization) {
        this.binType = binType;
        this.binTypeUtilization = binTypeUtilization;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public String getBinTypeUtilization() {
        return binTypeUtilization;
    }

    public void setBinTypeUtilization(String binTypeUtilization) {
        this.binTypeUtilization = binTypeUtilization;
    }

    @Override
    public String toString() {
        return "CapacityBinType{" +
                "binType='" + binType + '\'' +
                ", binTypeUtilization='" + binTypeUtilization + '\'' +
                '}';
    }
}
