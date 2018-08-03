package com.mushiny.wms.report.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.report.domain.CustomerShipmentPosition;
import com.mushiny.wms.report.domain.CustomerShipmentTest;
import com.mushiny.wms.report.domain.PickingOrderPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerShipmentRepository extends BaseRepository<CustomerShipmentTest, String> {

    @Query("select c from CustomerShipmentTest c where c.state < :state")
    List<CustomerShipmentTest> getCustomerShipment(@Param("state") int state);


    @Query("select s from PickingOrderPosition s where s.customerShipmentPosition = :customer")
    List<PickingOrderPosition> getByCustomerOrderPosition(@Param("customer") CustomerShipmentPosition customer);


}
