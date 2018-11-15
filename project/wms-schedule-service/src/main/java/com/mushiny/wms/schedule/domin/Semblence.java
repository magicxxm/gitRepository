package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MD_SEMBLENCE")
public class Semblence extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
