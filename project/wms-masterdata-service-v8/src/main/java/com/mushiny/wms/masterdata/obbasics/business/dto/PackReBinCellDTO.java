package com.mushiny.wms.masterdata.obbasics.business.dto;

//import com.mushiny.wms.masterdata.obbasics.crud.dto.CustomerShipmentDTO;

import java.io.Serializable;

public class PackReBinCellDTO implements Serializable {
    private static final long serialVersionUID = 1L;

//    private CustomerShipmentDTO customerShipmentDTO;

    private String reBinCellName;

//    public CustomerShipmentDTO getCustomerShipmentDTO() {
//        return customerShipmentDTO;
//    }
//
//    public void setCustomerShipmentDTO(CustomerShipmentDTO customerShipmentDTO) {
//        this.customerShipmentDTO = customerShipmentDTO;
//    }

    public String getReBinCellName() {
        return reBinCellName;
    }

    public void setReBinCellName(String reBinCellName) {
        this.reBinCellName = reBinCellName;
    }
}
