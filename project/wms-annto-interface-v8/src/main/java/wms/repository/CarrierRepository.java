package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.Carrier;

/**
 * Created by 123 on 2018/1/4.
 */
public interface CarrierRepository extends BaseRepository<Carrier,String> {


    @Query("select c from Carrier c where c.carrierNo = :carrierNo and c.warehouseId = :warehouseId")
    Carrier getByCarreierNoAndWarehouseId(@Param("carrierNo") String carrierNo,@Param("warehouseId")String warehouseId);
}
