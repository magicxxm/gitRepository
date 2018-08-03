package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * Created by Laptop-11 on 2017/6/9.
 */
public interface DeliveryTimeRepository extends BaseRepository<DeliveryTime, String> {
    @Query("select b from DeliveryTime b " +
            " where b.warehouseId = :warehouse and b.deliveryTime=:deliveryTime")
    DeliveryTime getByName(@Param("warehouse") String warehouse, @Param("deliveryTime") String deliveryTime);
}
