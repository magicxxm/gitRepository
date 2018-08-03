package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.BatterConfig;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BatterConfigDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal startNumber;

    @NotNull
    private BigDecimal endNumber;

    public BatterConfigDTO() {
    }

    public BatterConfigDTO(BatterConfig entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(BigDecimal startNumber) {
        this.startNumber = startNumber;
    }

    public BigDecimal getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(BigDecimal endNumber) {
        this.endNumber = endNumber;
    }
}
