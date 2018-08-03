package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.PackingRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackingRequestRepository extends BaseRepository<PackingRequest,String> {

    @Query("select p from PackingRequest p where p.customerShipment = :shipment")
    PackingRequest getByCustomerShipment(@Param("shipment") CustomerShipment shipment);
}
