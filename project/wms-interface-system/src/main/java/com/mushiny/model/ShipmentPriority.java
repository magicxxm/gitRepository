package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/21.
 */
@Entity
@Table(name = "OB_CUSTOMERSHIPMENT_PRIORITY")
public class ShipmentPriority extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADVANCE_TIME")
    private String advinceTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvinceTime() {
        return advinceTime;
    }

    public void setAdvinceTime(String advinceTime) {
        this.advinceTime = advinceTime;
    }
}
