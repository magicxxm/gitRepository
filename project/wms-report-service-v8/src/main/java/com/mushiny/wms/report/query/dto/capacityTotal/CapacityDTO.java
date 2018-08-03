package com.mushiny.wms.report.query.dto.capacityTotal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CapacityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zoneName;

    private Set<CapacityBinType> binTypes = new HashSet<>();

    private String totalUtilization;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Set<CapacityBinType> getBinTypes() {
        return binTypes;
    }

    public void setBinTypes(Set<CapacityBinType> binTypes) {
        this.binTypes = binTypes;
    }

    public String getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(String totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    @Override
    public String toString() {
        return "Capacity{" +
                "zoneName='" + zoneName + '\'' +
                ", binTypes=" + binTypes +
                ", totalUtilization='" + totalUtilization + '\'' +
                '}';
    }
}
