package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReceiveThresholdDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal threshold;

    public ReceiveThresholdDTO() {
    }

    public ReceiveThresholdDTO(ReceiveThreshold entity) {
        super(entity);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }
}
