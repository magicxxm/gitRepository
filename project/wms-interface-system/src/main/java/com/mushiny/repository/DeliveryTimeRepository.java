package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.DeliveryTime;
import com.mushiny.model.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 123 on 2018/3/25.
 */
public interface DeliveryTimeRepository extends BaseRepository<DeliveryTime,String> {

    @Query("select d from DeliveryTime d where d.deliveryTime = :deliveryTime and d.warehouse = :warehouse")
    List<DeliveryTime> getByDeliveryTimeAndWarehouse(@Param("deliveryTime")LocalDateTime localDateTime, @Param("warehouse")Warehouse warehouse);
}
