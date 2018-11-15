package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.DeliveryPoint;
import wms.domain.DeliverySortCode;
import wms.domain.DeliveryTime;

import java.util.List;

/**
 * Created by 123 on 2017/6/13.
 */
public interface DeliveryPointRepository extends BaseRepository<DeliveryPoint,String> {

    @Query("select d from DeliveryPoint d where d.sortCode = :sortCode and d.warehouseId = :warehouse")
    List<DeliveryPoint> getBySortCode(@Param("sortCode") DeliverySortCode code, @Param("warehouse") String warehouse);

    @Query("select d from DeliveryPoint d where d.deliveryTime = :time and d.sortCode=:sortCode and d.warehouseId = :warehouseId")
    DeliveryPoint getBySortCodeAndTime(@Param("sortCode") DeliverySortCode ds,
                                       @Param("time")DeliveryTime deliveryTime,
                                       @Param("warehouseId")String warehouseId);
}
