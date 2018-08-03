package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.LabelController;

import javax.validation.constraints.NotNull;

public class LabelControllerDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;


    @NotNull
    private String name;

    @NotNull
    private String addressIp;

    private int portNumber=4660;

    private int size = 0;


    public LabelControllerDTO() {
    }

    public LabelControllerDTO(LabelController entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressIp() {
        return addressIp;
    }

    public void setAddressIp(String addressIp) {
        this.addressIp = addressIp;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
