package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wms.domain.common.Warehouse;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2018/1/3.
 */
@Component
public class WarehouseBusiness {

    private final Logger log = LoggerFactory.getLogger(WarehouseBusiness.class);
    private final EntityManager manager;

    public WarehouseBusiness(EntityManager manager){
        this.manager = manager;
    }

    public Warehouse saveWarehouse(String warehouseNo){
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseNo(warehouseNo);
        warehouse.setName(warehouseNo);
        manager.persist(warehouse);
        return warehouse;
    }
}
