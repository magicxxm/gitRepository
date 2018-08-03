package com.mushiny.wms.masterdata.obbasics.business.dto;

import com.mushiny.wms.masterdata.obbasics.domain.Semblence;

import java.util.List;

/**
 * Created by prestonmax on 2017/6/1.
 */
public class SemblenceDTO {
    private String clientId;
    private int semblence;
    private String semblenceId;
    private String clientName;


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSemblenceId() {
        return semblenceId;
    }

    public void setSemblenceId(String semblenceId) {
        this.semblenceId = semblenceId;
    }

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
