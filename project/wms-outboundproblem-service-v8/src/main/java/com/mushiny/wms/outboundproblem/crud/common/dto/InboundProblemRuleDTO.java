package com.mushiny.wms.outboundproblem.crud.common.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;

import javax.validation.constraints.NotNull;

public class InboundProblemRuleDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    public InboundProblemRuleDTO() {
    }

    public InboundProblemRuleDTO(InboundProblemRule entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
