package com.mushiny.wms.masterdata.obbasics.business.pack;

import com.mushiny.wms.common.utils.RandomUtil;
//import com.mushiny.wms.context.PermissionsContext;
//import com.mushiny.wms.inbound.domain.UnitLoad;
//import com.mushiny.wms.inbound.repository.UnitLoadRepository;
//import com.mushiny.wms.masterdata.domain.StorageLocation;
//import com.mushiny.wms.masterdata.repository.StorageLocationRepository;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.domain.enums.PackRequestState;
import com.mushiny.wms.masterdata.obbasics.domain.enums.ShipmentState;
//import com.mushiny.wms.masterdata.obbasics.repository.PackingRequestRepository;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PackingRequestBusiness {

//    private final PermissionsContext permissionsContext;
//    private final PackingRequestRepository packingRequestRepository;
//    private final StorageLocationRepository storageLocationRepository;
//    private final UnitLoadRepository unitLoadRepository;
//
//
//    @Autowired
//    public PackingRequestBusiness(PackingRequestRepository packingRequestRepository,
//                                  PermissionsContext permissionsContext,
//                                  StorageLocationRepository storageLocationRepository,
//                                  UnitLoadRepository unitLoadRepository) {
//        this.packingRequestRepository = packingRequestRepository;
//        this.permissionsContext = permissionsContext;
//        this.storageLocationRepository = storageLocationRepository;
//        this.unitLoadRepository = unitLoadRepository;
//    }
//
//    public void checkPacked(PackingRequest pack,CustomerShipment shipment){
//        List<CustomerShipmentPosition> shipmentPositions = shipment.getPositions();
//        List<PackingRequestPosition> packPositions = pack.getPositions();
//        for(CustomerShipmentPosition shipmentPosition : shipmentPositions){
//            for(PackingRequestPosition packPosition : packPositions){
//                if(shipmentPosition.getId()
//                        .equalsIgnoreCase(packPosition.getCustomerShipmentPosition().getId())){
//                    if(shipmentPosition.getAmount().compareTo(packPosition.getAmountPacked()) == 0){
//                        shipmentPosition.setState(ShipmentState.PACKED);
//                        packPosition.setState(PackRequestState.PACKED.toString());
//                    }else {
//                        shipmentPosition.setState(ShipmentState.PACKING);
//                        packPosition.setState(PackRequestState.PACKING.toString());
//                    }
//                }
//            }
//        }
//        boolean packedFlag = true;
//        for(CustomerShipmentPosition shipmentPosition : shipmentPositions){
//            if(shipmentPosition.getState() != ShipmentState.PACKED){
//                packedFlag = false;
//                break;
//            }
//        }
//        if(packedFlag){
//            shipment.setState(ShipmentState.PACKED);
//            pack.setState(PackRequestState.PACKED.toString());
//            Warehouse warehouse = permissionsContext.getCurrentWarehouse();
//            StorageLocation bin = storageLocationRepository.getByName(warehouse,"PACKED");
//            UnitLoad unitLoad = pack.getUnitLoad();
//            unitLoad.setContainer(null);
//            unitLoad.setStorageLocation(bin);
//            unitLoadRepository.save(unitLoad);
//        }
//    }
//
//    public PackingRequest build(CustomerShipmentPosition shipmentPosition,
//                                ReBinRequestPosition reBinPosition,
//                                PackingStation packingStation){
//        PackingRequest pack = new PackingRequest();
//        while (true){
//            String packNo = RandomUtil.getPackNo();
//            if(packingRequestRepository.getByPackNo(packNo) == null){
//                pack.setPackingNo(packNo);
//                break;
//            }
//        }
//        pack.setCustomerShipment(shipmentPosition.getCustomerShipment());
//        pack.setState(PackRequestState.PACKING.toString());
//        pack.setUnitLoad(reBinPosition.getReBinUnitLoad().getUnitLoad());
//        pack.setOperator(permissionsContext.getCurrentUser());
//        pack.setPackingStation(packingStation);
//        pack.setClient(shipmentPosition.getClient());
//        pack.setWarehouse(shipmentPosition.getWarehouse());
//        PackingRequestPosition packPosition = build(pack,shipmentPosition);
//        pack.getPositions().add(packPosition);
//        return pack;
//    }
//
//    public PackingRequestPosition build(PackingRequest pack,
//                                        CustomerShipmentPosition shipmentPosition){
//        PackingRequestPosition packPosition = new PackingRequestPosition();
//        packPosition.setAmount(shipmentPosition.getAmount());
//        packPosition.setAmountPacked(BigDecimal.ONE);
//        packPosition.setState(PackRequestState.PACKING.toString());
//        packPosition.setCustomerShipmentPosition(shipmentPosition);
//        packPosition.setItemData(shipmentPosition.getItemData());
//        packPosition.setLot(shipmentPosition.getLot());
//        packPosition.setClient(pack.getClient());
//        packPosition.setWarehouse(pack.getWarehouse());
//        return packPosition;
//    }
}
