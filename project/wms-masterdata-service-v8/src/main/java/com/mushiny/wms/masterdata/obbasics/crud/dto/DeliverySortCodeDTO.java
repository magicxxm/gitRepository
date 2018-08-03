package com.mushiny.wms.masterdata.obbasics.crud.dto;


import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;

import javax.validation.constraints.NotNull;


/**
 * Created by Laptop-11 on 2017/6/8.
 */

public class DeliverySortCodeDTO extends BaseWarehouseAssignedDTO{
    private static final long serialVersionUID = 1L;
    private String code;
    private String description;

    public DeliverySortCodeDTO(){

    }
    public DeliverySortCodeDTO(DeliverySortCode entity){
        super(entity);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
