package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;



@Entity
@Table(name = "MD_TIMECONFIG")
public class TimeConfig extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "REFRESH_TIME")
    private String refreshTime;
    @Column(name = "REFRESH_DAY")
    private int refreshDay;

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getRefreshDay() {
        return refreshDay;
    }

    public void setRefreshDay(int refreshDay) {
        this.refreshDay = refreshDay;
    }
}
