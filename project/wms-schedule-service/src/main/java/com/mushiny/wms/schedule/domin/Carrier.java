package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_CARRIER")
public class Carrier extends BaseWarehouseAssignedEntity {

    @Column(name = "CARRIER_NO", nullable = false)
    private String carrierNo;

    @Column(name = "NAME", nullable = false)
    private String name;

    public String getCarrierNo() {
        return carrierNo;
    }

    public void setCarrierNo(String carrierNo) {
        this.carrierNo = carrierNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
