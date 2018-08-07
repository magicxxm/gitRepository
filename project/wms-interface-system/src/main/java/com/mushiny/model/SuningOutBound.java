package com.mushiny.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by 123 on 2018/5/2.
 */
@Entity
@Table(name = "SUNING_ZRFC_AGV_OUTBOUND")
public class SuningOutBound implements Serializable{

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "OUT_ORDER_CODE")
    private String orderCode;

    @Column(name = "ZTBPRI")
    private String ztbpri;

    @Column(name = "ZDEZGTIM")
    private String deliveryTime;

    @Column(name = "NEED_RESPONSE")
    private String needResponse;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getZtbpri() {
        return ztbpri;
    }

    public void setZtbpri(String ztbpri) {
        this.ztbpri = ztbpri;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getNeedResponse() {
        return needResponse;
    }

    public void setNeedResponse(String needResponse) {
        this.needResponse = needResponse;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}