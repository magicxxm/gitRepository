package com.mushiny.wms.report.query.dto.picked;

import java.io.Serializable;

public class Zones implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zoneName;

    private PickAmount pickAmount;

    public Zones() {
    }

    public Zones(String zoneName, PickAmount pickAmount) {
        this.zoneName = zoneName;
        this.pickAmount = pickAmount;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public PickAmount getPickAmount() {
        return pickAmount;
    }

    public void setPickAmount(PickAmount pickAmount) {
        this.pickAmount = pickAmount;
    }

    @Override
    public String toString() {
        return "Zones{" +
                "zoneName='" + zoneName + '\'' +
                ", pickAmount=" + pickAmount +
                '}';
    }
}
