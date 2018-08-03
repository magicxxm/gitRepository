package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.RebinUnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RebinUnitLoadRepository extends BaseRepository<RebinUnitLoad, String> {
    @Query("select r from RebinUnitLoad r where r.customerShipmentNumber = :shipmentNo and r.warehouseId = :warehouseId")
    RebinUnitLoad getByShipmentNo(@Param("shipmentNo")String shipmentNo, @Param("warehouseId")String warehouseId);
}
