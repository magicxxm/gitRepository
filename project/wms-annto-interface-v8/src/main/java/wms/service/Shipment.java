package wms.service;

import org.springframework.web.bind.annotation.RequestBody;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.CustomerOrderDTO;
import wms.crud.dto.ShipmentCancelDTO;
import wms.crud.dto.ShipmentConfirmDTO;
import wms.crud.dto.ShipmentUpdateDTO;
import wms.domain.common.CustomerShipment;

public interface Shipment {

//    AccessDTO update(CustomerOrderDTO dto);

    AccessDTO update(ShipmentUpdateDTO DTO);

    void confirm(String  shipmentNo,String operatorNo, String operatorTime);

    AccessDTO cancel(ShipmentCancelDTO shipmentCancelDTO);

    void confirm(ShipmentConfirmDTO shipmentConfirmDTO);

    void confirmShipment(CustomerShipment shipment);
}
