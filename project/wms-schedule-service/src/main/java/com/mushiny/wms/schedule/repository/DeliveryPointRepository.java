package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.DeliveryPoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryPointRepository extends BaseRepository<DeliveryPoint,String> {

    @Query("select d from DeliveryPoint d where d.sortCode.code not in ('REPLENISHMENT','TABLE') and d.warehouseId = :warehouse")
    List<DeliveryPoint> getBySortCode(@Param("warehouse") String warehouse);

}
