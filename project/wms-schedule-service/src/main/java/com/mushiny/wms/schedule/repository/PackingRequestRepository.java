package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerShipment;
import com.mushiny.wms.schedule.domin.PackingRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2017/5/16.
 */
public interface PackingRequestRepository extends BaseRepository<PackingRequest, String> {

    @Query("select p from PackingRequest p where p.packingNo=:packNo")
    PackingRequest getByPackingNo(@Param("packNo") String packNo);

    @Query("select p from PackingRequest p where p.customerShipment=:shipment")
    PackingRequest getByCustomerShipment(@Param("shipment") CustomerShipment shipment);
}
