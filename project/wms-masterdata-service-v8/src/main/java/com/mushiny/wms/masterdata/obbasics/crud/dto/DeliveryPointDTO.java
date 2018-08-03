package com.mushiny.wms.masterdata.obbasics.crud.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryPoint;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by Laptop-11 on 2017/6/8.
 */

public class DeliveryPointDTO extends BaseWarehouseAssignedDTO {

    private static final long serialVersionUID = 1L;
    private String sortCodeId;
    private DeliverySortCodeDTO deliverySortCode;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String carrierId;
    private CarrierDTO carrier;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String time;
    private DeliveryTimeDTO deliveryTime;

    public String getSortCodeId() {
        return sortCodeId;
    }

    public void setSortCodeId(String sortCodeId) {
        this.sortCodeId = sortCodeId;
    }

    public DeliverySortCodeDTO getDeliverySortCode() {
        return deliverySortCode;
    }

    public void setDeliverySortCode(DeliverySortCodeDTO deliverySortCode) {
        this.deliverySortCode = deliverySortCode;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public CarrierDTO getCarrier() {
        return carrier;
    }

    public void setCarrier(CarrierDTO carrier) {
        this.carrier = carrier;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DeliveryTimeDTO getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(DeliveryTimeDTO deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public DeliveryPointDTO() {

    }

    public DeliveryPointDTO(DeliveryPoint entity) {
        super(entity);
    }


}
