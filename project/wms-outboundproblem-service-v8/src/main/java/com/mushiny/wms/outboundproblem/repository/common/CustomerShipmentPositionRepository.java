package com.mushiny.wms.outboundproblem.repository.common;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipmentPosition;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerShipmentPositionRepository extends BaseRepository<CustomerShipmentPosition, String> {

    @Query(" select c from CustomerShipmentPosition c where c.warehouseId = :warehouseId and c.customerShipment.shipmentNo = :shipmentNo ")
    List<CustomerShipmentPosition> getByShipmentID(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query(" select c from CustomerShipmentPosition c where c.warehouseId=:warehouseId and c.customerShipment.shipmentNo=:shipmentNo and c.itemData=:itemData")
    CustomerShipmentPosition getByShipmentAndItemData(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo, @Param("itemData") ItemData itemData);
}
