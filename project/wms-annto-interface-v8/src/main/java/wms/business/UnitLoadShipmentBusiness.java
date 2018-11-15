package wms.business;

import org.springframework.stereotype.Component;
import wms.domain.common.UnitLoadShipment;
import wms.repository.common.UnitLoadShipmentRepository;
import wms.service.EntityGenerator;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2017/5/22.
 */
@Component
public class UnitLoadShipmentBusiness {

    private final UnitLoadShipmentRepository unitLoadShipmentRepository;
    private final EntityManager manager;
    private final EntityGenerator entityGenerator;

    public UnitLoadShipmentBusiness(UnitLoadShipmentRepository unitLoadShipmentRepository,
                                    EntityManager manager,
                                    EntityGenerator entityGenerator) {
        this.unitLoadShipmentRepository = unitLoadShipmentRepository;
        this.manager = manager;
        this.entityGenerator = entityGenerator;
    }

    public void create(String unitLoadId, String shipmentId){
        UnitLoadShipment us = new UnitLoadShipment();
        us.setUnitLoadId(unitLoadId);
        us.setCustometShipmentId(shipmentId);

        manager.persist(us);
        manager.flush();

    }
}
