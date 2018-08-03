package com.mushiny.wms.tot.jobthreshold.domain;


import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TOT_THRESHOLD")
public class Jobthreshold extends BaseWarehouseAssignedEntity {
    @Column(name = "THRESHOLD_A", nullable = false)
    private int thresholdA;
    @Column(name = "THRESHOLD_B", nullable = false)
    private int thresholdB;

    public int getThresholdA() {
        return thresholdA;
    }

    public void setThresholdA(int thresholdA) {
        this.thresholdA = thresholdA;
    }

    public int getThresholdB() {
        return thresholdB;
    }

    public void setThresholdB(int thresholdB) {
        this.thresholdB = thresholdB;
    }
}
