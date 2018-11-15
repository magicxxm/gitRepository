package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.CustomerShipment;
import wms.domain.common.PackingRequest;
import wms.domain.common.PackingStation;
import wms.domain.common.UnitLoad;

/**
 * Created by 123 on 2017/5/16.
 */
public interface PackingRequestRepository extends BaseRepository<PackingRequest,String> {

    @Query("select p from PackingRequest p where p.packingNo=:packNo")
    PackingRequest getByPackingNo(@Param("packNo") String packNo);

    @Query("select p from PackingRequest p where p.customerShipment=:shipment")
    PackingRequest getByCustomerShipment(@Param("shipment") CustomerShipment shipment);

    @Query("select p from PackingRequest p where p.fromUnitLoad=:fromUnitLoad and p.packingStation=:station")
    PackingRequest getByToUnitAndStation(@Param("fromUnitLoad")UnitLoad fromUnitLoad, @Param("station")PackingStation station);
}
