package com.mushiny.wms.masterdata.obbasics.crud.dto;


import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Laptop-11 on 2017/6/8.
 */

public class DeliveryTimeDTO extends BaseWarehouseAssignedDTO{
    private static final long serialVersionUID = 1L;
    private String deliveryTime;

    public DeliveryTimeDTO(){

    }
    public DeliveryTimeDTO(DeliveryTime entity){
        super(entity);
    }
    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


}
