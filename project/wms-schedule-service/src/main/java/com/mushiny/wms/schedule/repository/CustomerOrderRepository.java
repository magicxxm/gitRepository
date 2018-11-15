package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerOrderRepository extends BaseRepository<CustomerOrder, String> {

    @Query("select c from CustomerOrder c where c.orderNo = :orderNo")
    CustomerOrder getByOrderNo(@Param("orderNo") String orderNo);

    @Query("select c from CustomerOrder c where c.state < :state")
    List<CustomerOrder> getByState(@Param("state") int released);
}
