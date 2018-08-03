package com.mushiny.wms.report.query.dto.picked;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Picked implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ppName;

    private Timestamp deliveryDate; //发货时间

    private String zoneName; //区域

    private BigDecimal total = BigDecimal.ZERO; //拣货总数量

    private BigDecimal picked = BigDecimal.ZERO; //已拣货数量

    private BigDecimal notPicked = BigDecimal.ZERO; //未拣货数量

    private BigDecimal pending = BigDecimal.ZERO;// 等待状态

    public Picked() {
    }


    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

//    public LocalDateTime getDeliveryDate() {
//        return deliveryDate;
//    }
//
//    public void setDeliveryDate(String deliveryDate) {
//        this.deliveryDate = LocalDateTime.parse(deliveryDate);
//    }
//    public void setDeliveryDate(LocalDateTime deliveryDate) {
//        this.deliveryDate = deliveryDate;
//    }

    public BigDecimal getPending() {
        return pending;
    }

    public void setPending(BigDecimal pending) {
        this.pending = pending;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPicked() {
        return picked;
    }

    public void setPicked(BigDecimal picked) {
        this.picked = picked;
    }

    public BigDecimal getNotPicked() {
        return notPicked;
    }

    public void setNotPicked(BigDecimal notPicked) {
        this.notPicked = notPicked;
    }

    @Override
    public String toString() {
        return "PickedDTO{" +
                "ppName='" + ppName + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", zoneName='" + zoneName + '\'' +
                ", total=" + total +
                ", picked=" + picked +
                ", notPicked=" + notPicked +
                '}';
    }
}
