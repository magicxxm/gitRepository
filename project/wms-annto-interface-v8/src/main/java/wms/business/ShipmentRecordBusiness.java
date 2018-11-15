package wms.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.domain.common.CustomerShipment;
import wms.domain.common.CustomerShipmentRecord;
import wms.repository.common.CustomerShipmentRecordRepository;
import wms.repository.common.UserRepository;

/**
 * Created by 123 on 2017/5/22.
 */
@Component
public class ShipmentRecordBusiness {

    private final CustomerShipmentRecordRepository shipmentRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShipmentRecordBusiness(CustomerShipmentRecordRepository shipmentRecordRepository,
                                  UserRepository userRepository) {
        this.shipmentRecordRepository = shipmentRecordRepository;
        this.userRepository = userRepository;
    }

    public CustomerShipmentRecord getShipmentRecord(CustomerShipment shipment, String stationName, String operatorId, String stateName){
        CustomerShipmentRecord shipmentRecord = new CustomerShipmentRecord();
        shipmentRecord.setShipmentId(shipment.getId());
        shipmentRecord.setStateName(stateName);
        shipmentRecord.setState(shipment.getState());
        shipmentRecord.setStationName(stationName);
        if(operatorId != null && !"".equals(operatorId)){
            shipmentRecord.setOperator(userRepository.findOne(operatorId));
        }
        shipmentRecord.setClientId(shipment.getClientId());
        shipmentRecord.setWarehouseId(shipment.getWarehouseId());
        shipmentRecordRepository.saveAndFlush(shipmentRecord);
        return shipmentRecord;
    }
}
