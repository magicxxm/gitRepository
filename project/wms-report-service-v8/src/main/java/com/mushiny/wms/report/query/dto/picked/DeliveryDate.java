package com.mushiny.wms.report.query.dto.picked;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DeliveryDate implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime deliveryDate;

    private PickAmount pickAmount;

    public DeliveryDate() {
    }

    public DeliveryDate(LocalDateTime deliveryDate, PickAmount pickAmount) {
        this.deliveryDate = deliveryDate;
        this.pickAmount = pickAmount;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public PickAmount getPickAmount() {
        return pickAmount;
    }

    public void setPickAmount(PickAmount pickAmount) {
        this.pickAmount = pickAmount;
    }
}
