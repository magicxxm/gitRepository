package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.DeliveryTime;

import java.util.List;

/**
 * Created by 123 on 2017/6/13.
 */
public interface DeliveryTimeRepository extends BaseRepository<DeliveryTime,String> {

    @Query("select d from DeliveryTime d where d.warehouseId=:warehouseId and d.entityLock=:entityLock")
    List<DeliveryTime> getAllByWarehouseIdAndEntityLock(@Param("warehouseId") String warehouseId, @Param("entityLock") int entityLock);

    @Query("select d from DeliveryTime d where d.deliveryTime = :time and d.warehouseId = :warehouseId")
    DeliveryTime getByTimeAndWarehouseId(@Param("time") String exsd, @Param("warehouseId") String warehouseId);
}
