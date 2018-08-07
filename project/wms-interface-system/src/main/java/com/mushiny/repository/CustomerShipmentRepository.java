package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.CustomerShipment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2017/5/16.
 */
public interface CustomerShipmentRepository extends BaseRepository<CustomerShipment,String> {

    @Query("select c from CustomerShipment c where c.shipmentNo=:shipmentNo")
    CustomerShipment getByShipmentNo(@Param("shipmentNo")String shipmentNo);


    @Query("select c from CustomerShipment c where c.shipmentNo=:shipmentNo and c.warehouseId = :warehouseId")
    CustomerShipment getByShipmentNoAndWarehouse(@Param("shipmentNo")String shipmentNo, @Param("warehouseId") String id);
}
