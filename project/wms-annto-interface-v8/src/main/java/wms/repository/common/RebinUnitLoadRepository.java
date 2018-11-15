package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.RebinUnitLoad;

import java.util.List;

public interface RebinUnitLoadRepository extends BaseRepository<RebinUnitLoad, String> {

    @Query("select r from RebinUnitLoad r where r.customerShipmentNumber = :shipmentNo")
    RebinUnitLoad getByShipmentNo(@Param("shipmentNo") String shipmentNo);

}
