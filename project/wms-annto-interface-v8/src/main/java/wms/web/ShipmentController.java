package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.crud.dto.ShipmentConfirmDTO;
import wms.service.Shipment;
import wms.common.crud.AccessDTO;
import wms.crud.dto.ShipmentCancelDTO;
import wms.crud.dto.ShipmentUpdateDTO;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/robot/shipment")
public class ShipmentController {

    private final Shipment shipment;

    @Autowired
    public ShipmentController(Shipment shipment) {
        this.shipment = shipment;
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> shipmentUpdate(@RequestBody ShipmentUpdateDTO dto) {
        return ResponseEntity.ok(shipment.update(dto));
    }

    //不用
   /* @RequestMapping(value = "/confirm",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> shipmentConfirm(@RequestParam String shipmentNo,@RequestParam String operatorNo,@RequestParam String operatorTime) {
        shipment.confirm(shipmentNo,operatorNo,operatorTime);
        return ResponseEntity.ok().build();
    }*/

    //订单完成，向安得发送信息
    @RequestMapping(value = "/confirm",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmShipment(@RequestBody ShipmentConfirmDTO shipmentConfirmDTO ) {
        shipment.confirm(shipmentConfirmDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/cancel",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> shipmentCancel(@RequestBody ShipmentCancelDTO shipmentCancelDTO) {
        return ResponseEntity.ok(shipment.cancel(shipmentCancelDTO));
    }

}
