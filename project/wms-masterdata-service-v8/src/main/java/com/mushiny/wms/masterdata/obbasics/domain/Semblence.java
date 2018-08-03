package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by prestonmax on 2017/6/1.
 */
@Entity
@Table(name = "MD_SEMBLENCE")
public class Semblence extends BaseEntity {
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Column(name = "SEMBLENCE")
    private int semblence;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getSemblence() {
        return semblence;
    }

    public void setSemblence(int semblence) {
        this.semblence = semblence;
    }
}
