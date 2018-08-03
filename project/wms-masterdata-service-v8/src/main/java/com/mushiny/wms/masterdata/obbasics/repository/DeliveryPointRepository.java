package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryPoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Created by Laptop-11 on 2017/6/9.
 */
public interface DeliveryPointRepository extends BaseRepository<DeliveryPoint, String> {
    @Query("select b from DeliveryPoint b " +
            " where b.warehouseId = :warehouse and b.deliveryTime.id = :deliveryTime and b.deliverySortCode.id=:deliverySortCode")
    DeliveryPoint getByName(@Param("warehouse") String warehouse,
                            @Param("deliveryTime") String deliveryTime,
                            @Param("deliverySortCode") String deliverySortCode);
}
