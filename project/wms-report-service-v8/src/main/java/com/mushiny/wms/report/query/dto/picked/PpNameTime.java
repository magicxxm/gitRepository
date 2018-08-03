package com.mushiny.wms.report.query.dto.picked;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PpNameTime implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ppName;

    private LocalDateTime deliveryDate;

    public PpNameTime() {
    }

    public PpNameTime(String ppName, LocalDateTime deliveryDate) {
        this.ppName = ppName;
        this.deliveryDate = deliveryDate;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PpNameTime)) return false;

        PpNameTime that = (PpNameTime) o;

        if (getPpName() != null ? !getPpName().equals(that.getPpName()) : that.getPpName() != null) return false;
        return getDeliveryDate() != null ? getDeliveryDate().equals(that.getDeliveryDate()) : that.getDeliveryDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getPpName() != null ? getPpName().hashCode() : 0;
        result = 31 * result + (getDeliveryDate() != null ? getDeliveryDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PpNameTime{" +
                "ppName='" + ppName + '\'' +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}
