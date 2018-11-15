package wms.repository.common;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import wms.common.respository.BaseRepository;
import wms.domain.common.UnitLoadShipment;

import java.util.List;

/**
 * Created by 123 on 2017/5/11.
 */
public interface UnitLoadShipmentRepository extends BaseRepository<UnitLoadShipment,String> {
    /**
     * 查找
     * @param unitLoadId
     * @param shipmentId
     * @return
     */
    @Query("select us from UnitLoadShipment us where us.unitLoadId=:unitLoadId and us.custometShipmentId=:shipmentId")
    UnitLoadShipment getByUnitLoadIdAndShipmentId(@Param("unitLoadId") String unitLoadId, @Param("shipmentId") String shipmentId);


    @Query("select us from UnitLoadShipment us where us.custometShipmentId=:shipmentId")
    UnitLoadShipment getByCustometShipmentId(@Param("shipmentId") String shipmentId);

    @Query("select us from UnitLoadShipment us where us.custometShipmentId=:shipmentId")
    List<UnitLoadShipment> getAllByCustometShipmentId(@Param("shipmentId") String shipmentId);

    @Transactional
    @Modifying
    @Query("DELETE from UnitLoadShipment where custometShipmentId= :shipmentId")
    void deleteCustometShipment(@Param("shipmentId") String shipmentId);

    @Modifying
    @Query("update UnitLoadShipment set unitLoadId=:unitLoadId where custometShipmentId= :shipmentId")
    void updateCustometShipment(@Param("unitLoadId") String unitLoadId, @Param("shipmentId") String shipmentId);

    @Query("select us from UnitLoadShipment us where us.unitLoadId=:unitLoadId")
    UnitLoadShipment getByUnitLoadId(@Param("unitLoadId") String unitLoadId);
}
