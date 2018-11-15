package wms.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.domain.common.*;
import wms.domain.enums.PackRequestState;
import wms.repository.common.*;
import wms.service.EntityGenerator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2017/5/16.
 */
@Component
public class PackingRequestBusiness {

    private final PackingRequestRepository packingRequestRepository;
    private final EntityManager manager;
    private final EntityGenerator entityGenerator;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final PackingRequestPositionRepository positionRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final StorageLocationRepository storageLocationRepository;

    @Autowired
    public PackingRequestBusiness(PackingRequestRepository packingRequestRepository,
                                  EntityManager manager,
                                  EntityGenerator entityGenerator,
                                  UserRepository userRepository,
                                  ApplicationContext applicationContext,
                                  PackingRequestPositionRepository positionRepository,
                                  UnitLoadRepository unitLoadRepository,
                                  StorageLocationRepository storageLocationRepository) {
        this.packingRequestRepository = packingRequestRepository;
        this.manager = manager;
        this.entityGenerator = entityGenerator;
        this.userRepository = userRepository;
        this.applicationContext = applicationContext;
        this.positionRepository = positionRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.storageLocationRepository = storageLocationRepository;
    }

    public PackingRequest buildPackingRequest(CustomerShipmentPosition shipmentPosition, UnitLoad unitLoad, PackingStation packingStation) {
        String operatorId = applicationContext.getCurrentUser();
        PackingRequest pack = entityGenerator.generateEntity(PackingRequest.class);
        CustomerShipment customerShipment = shipmentPosition.getCustomerShipment();
        String packNo = customerShipment.getShipmentNo();
        pack.setBoxType(customerShipment.getBoxType());
        pack.setCustomerShipment(customerShipment);
        pack.setFromUnitLoad(unitLoad);
        pack.setOperator(userRepository.findOne(operatorId));
        pack.setPackingStation(packingStation);
        pack.setState(PackRequestState.PACKING.toString());
        pack.setWarehouseId(applicationContext.getCurrentWarehouse());
        pack.setClientId(applicationContext.getCurrentClient());
        pack.setVersion(0);
        pack.setPackingNo(packNo);
        pack.setToUnitLoad(createUnitLoad(packNo,packingStation,customerShipment.getClientId(),customerShipment.getWarehouseId()));
        manager.persist(pack);
        manager.flush();
        return pack;
    }

    public PackingRequestPosition buildPackingRequestPosition(PackingRequest pack,CustomerShipmentPosition shipmentPosition){
        PackingRequestPosition packPosition = entityGenerator.generateEntity(PackingRequestPosition.class);
        packPosition.setAmount(BigDecimal.ONE);
        packPosition.setAmountPacked(BigDecimal.ONE);
        packPosition.setState(PackRequestState.PACKED.toString());
        packPosition.setCustomerShipmentPosition(shipmentPosition);
        packPosition.setItemData(shipmentPosition.getItemData());
        packPosition.setClientId(pack.getClientId());
        packPosition.setWarehouseId(pack.getWarehouseId());
        packPosition.setPackingRequest(pack);
        manager.persist(packPosition);
        manager.flush();
        return packPosition;
    }

   /* public UnitLoad findUnitLoad(String packNo,PackingStation packingStation, String clientId, String warehouseId){
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouseId,"Packed");
        if(storageLocation != null){
            UnitLoad unitLoad = unitLoadRepository.findUnitLoadByStorage(storageLocation,warehouseId);
            if(unitLoad != null){
                return unitLoad;
            }
            UnitLoad u = entityGenerator.generateEntity(UnitLoad.class);
            u.setLabel(packNo);
            u.setStorageLocation(storageLocation);
            u.setClientId(clientId);
            u.setStationName(packingStation.getName());
            u.setWarehouseId(warehouseId);
            u.setVersion(0);

            manager.persist(u);
            manager.flush();
            return u;
        }else {
            return createUnitLoad(packNo,packingStation,clientId,warehouseId);
        }
    }*/

    public UnitLoad createUnitLoad(String packNo, PackingStation packingStation, String clientId, String warehouseId) {
        UnitLoad unitLoad = unitLoadRepository.getByLabel(packNo);
        if(unitLoad != null){
            return unitLoad;
        }
        UnitLoad u = entityGenerator.generateEntity(UnitLoad.class);
        u.setLabel(packNo);
        u.setStorageLocation(createStorageLocation(clientId,warehouseId));
        u.setCarrierUnitLoadId(createCarrierUnitLoad(clientId,warehouseId).getCarrierUnitLoadId());
        u.setClientId(clientId);
        u.setStationName(packingStation.getName());
        u.setWarehouseId(warehouseId);
        u.setVersion(0);

        manager.persist(u);
        manager.flush();
        return u;
    }

    private UnitLoad createCarrierUnitLoad(String clientId, String warehouseId) {
        UnitLoad unitLoad = unitLoadRepository.getByLabel("000000000000000000");
        if(unitLoad != null){
            return unitLoad;
        }
        UnitLoad u = entityGenerator.generateEntity(UnitLoad.class);
        u.setLabel("000000000000000000");
        u.setStorageLocation(createStorageLocation(clientId,warehouseId));
        u.setClientId(clientId);
        u.setWarehouseId(warehouseId);
        u.setVersion(0);

        manager.persist(u);
        manager.flush();
        return u;
    }


    public StorageLocation createStorageLocation(String clientId,String warehouseId){
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouseId,"Packed");
        if(storageLocation != null){
            return storageLocation;
        }
        StorageLocation s = entityGenerator.generateEntity(StorageLocation.class);
        s.setName("Packed");
        s.setClientId(clientId);
        s.setWarehouseId(warehouseId);
        manager.persist(s);
        manager.flush();
        return s;
    }

    //pick完直接包装，完成后操作packingRequestposition
    public PackingRequestPosition createPackingRequestPosition(PackingRequest pack,CustomerShipmentPosition shipmentPosition){
        PackingRequestPosition packPosition = entityGenerator.generateEntity(PackingRequestPosition.class);
        packPosition.setAmount(shipmentPosition.getAmount());
        packPosition.setAmountPacked(shipmentPosition.getAmount());
        packPosition.setState(PackRequestState.PACKED.toString());
        packPosition.setCustomerShipmentPosition(shipmentPosition);
        packPosition.setItemData(shipmentPosition.getItemData());
        packPosition.setClientId(pack.getClientId());
        packPosition.setWarehouseId(pack.getWarehouseId());
        packPosition.setVersion(0);
        packPosition.setPackingRequest(pack);
        manager.persist(packPosition);
        manager.flush();
        return packPosition;
    }

    public void cleanPackingRequest(CustomerShipment shipment){
        PackingRequest request = packingRequestRepository.getByCustomerShipment(shipment);
        if (request != null) {
            if (!request.getState().equalsIgnoreCase(PackRequestState.PACKED.toString())) {
                packingRequestRepository.delete(request);
            }
        }
    }

    public void cleanPackingRequestPosition(PackingRequest packingRequest) {
        List<PackingRequestPosition> positionList = positionRepository.getByPackingRequest(packingRequest);
        if(positionList != null && positionList.size() > 0){
            for (PackingRequestPosition p:positionList) {
                positionRepository.delete(p);
            }
        }
    }
}
