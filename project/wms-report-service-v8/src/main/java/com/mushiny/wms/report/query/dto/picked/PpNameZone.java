package com.mushiny.wms.report.query.dto.picked;

import java.io.Serializable;

public class PpNameZone implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ppName;

    private String zoneName;

    public PpNameZone() {
    }

    public PpNameZone(String ppName, String zoneName) {
        this.ppName = ppName;
        this.zoneName = zoneName;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PpNameZone)) return false;

        PpNameZone that = (PpNameZone) o;

        if (getPpName() != null ? !getPpName().equals(that.getPpName()) : that.getPpName() != null) return false;
        return getZoneName() != null ? getZoneName().equals(that.getZoneName()) : that.getZoneName() == null;
    }

    @Override
    public int hashCode() {
        int result = getPpName() != null ? getPpName().hashCode() : 0;
        result = 31 * result + (getZoneName() != null ? getZoneName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PpNameZone{" +
                "ppName='" + ppName + '\'' +
                ", zoneName='" + zoneName + '\'' +
                '}';
    }
}
