package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerShipment;
import com.mushiny.wms.schedule.domin.CustomerShipmentPosition;
import com.mushiny.wms.schedule.domin.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerShipmentPositionRepository extends BaseRepository<CustomerShipmentPosition,String> {

    @Query("select c from CustomerShipmentPosition c where c.itemData=:itemData and c.customerShipment=:shipment")
    CustomerShipmentPosition getByItemDataAndShipment(@Param("itemData") ItemData itemData, @Param("shipment") CustomerShipment shipment);

    @Query("select c from CustomerShipmentPosition c where c.customerShipment=:shipment")
    List<CustomerShipmentPosition> getByShipment(@Param("shipment") CustomerShipment shipment);

    @Query("update CustomerShipmentPosition po set po.state=:state where po.customerShipment=:shipment")
    void setPositionStateByShip(@Param("shipment") CustomerShipment shipment, @Param("state") int state);
}
