package com.mushiny.wms.report.query.dto.capacityTotal;

import java.io.Serializable;
import java.math.BigDecimal;

public class Capacity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zoneName;

    private String binType;

    private BigDecimal binTypeUtilization;

    private BigDecimal totalUtilization;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public BigDecimal getBinTypeUtilization() {
        return binTypeUtilization;
    }

    public void setBinTypeUtilization(BigDecimal binTypeUtilization) {
        this.binTypeUtilization = binTypeUtilization.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(BigDecimal totalUtilization) {
        this.totalUtilization = totalUtilization.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "Capacity{" +
                "zoneName='" + zoneName + '\'' +
                ", binType='" + binType + '\'' +
                ", binTypeUtilization=" + binTypeUtilization +
                ", totalUtilization=" + totalUtilization +
                '}';
    }
}
