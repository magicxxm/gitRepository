package com.mushiny.wms.outboundproblem.repository.common;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerShipmentRepository extends BaseRepository<CustomerShipment, String> {

    @Query("select c from CustomerShipment c where c.shipmentNo = :shipmentNo")
    CustomerShipment getByShipmentNo(@Param("shipmentNo") String shipmentNo);
}
