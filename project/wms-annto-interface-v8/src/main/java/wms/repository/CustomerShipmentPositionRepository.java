package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ItemData;
import wms.domain.common.CustomerShipment;
import wms.domain.common.CustomerShipmentPosition;

import java.util.List;

/**
 * Created by 123 on 2017/5/17.
 */
public interface CustomerShipmentPositionRepository extends BaseRepository<CustomerShipmentPosition,String> {

    @Query("select c from CustomerShipmentPosition c where c.itemData=:itemData and c.customerShipment=:shipment")
    CustomerShipmentPosition getByItemDataAndShipment(@Param("itemData") ItemData itemData, @Param("shipment") CustomerShipment shipment);

    @Query("select c from CustomerShipmentPosition c where c.customerShipment=:shipment")
    List<CustomerShipmentPosition> getByShipment(@Param("shipment") CustomerShipment shipment);

    @Query("update CustomerShipmentPosition po set po.state=:state where po.customerShipment=:shipment")
    void setPositionStateByShip(@Param("shipment") CustomerShipment shipment, @Param("state") int state);
}
