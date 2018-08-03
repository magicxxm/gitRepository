package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CarrierDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String carrierNo;

    @NotNull
    private String name;

    public CarrierDTO() {
    }

    public CarrierDTO(Carrier entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public void setCarrierNo(String carrierNo) {
        this.carrierNo = carrierNo;
    }
}
