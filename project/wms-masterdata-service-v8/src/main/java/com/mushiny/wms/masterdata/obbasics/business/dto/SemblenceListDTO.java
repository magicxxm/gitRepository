package com.mushiny.wms.masterdata.obbasics.business.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;

/**
 * Created by prestonmax on 2017/6/3.
 */
public class SemblenceListDTO{
    private int semblence;
    private String id;
    private String clientName;
    private String clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getSemblence() {
        return semblence;
    }

    public void setSemblence(int semblence) {
        this.semblence = semblence;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
