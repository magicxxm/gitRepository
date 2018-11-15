package wms.repository.common;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.CustomerShipment;
import wms.domain.common.DigitallabelShipment;
import wms.domain.common.PickPackWall;
import wms.domain.common.User;


public interface DigitallabelShipmentRepository extends BaseRepository<DigitallabelShipment, String> {

    @Query("select d from DigitallabelShipment d where d.state = 1 and d.digitalLabel2 = :id")
    DigitallabelShipment getByDigitalId(@Param("id") String id);

    @Modifying
    @Query("update DigitallabelShipment set  state = 2,operator =:user where shipment = :shipment")
    void deleteDigitalShipmentByshipment(@Param("shipment") CustomerShipment shipment, @Param("user") User user);

    @Query("select c from DigitallabelShipment c where  c.state = 1 and c.pickPackWallId = :pickPackWallId")
    DigitallabelShipment getDigitallabelByWall(@Param("pickPackWallId") String pickPackWallId);

    @Modifying
    @Query("update DigitallabelShipment set entityLock = 2 , state = 3 where shipment = :shipment")
    void updateDigitalShipmentByshipment(@Param("shipment") CustomerShipment shipment);
}
