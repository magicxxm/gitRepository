package com.mushiny.wms.report.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.report.domain.CustomerShipmentPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerShipmentPositionRepository extends BaseRepository<CustomerShipmentPosition, String> {


    @Query("select s from CustomerShipmentPosition s where s.customerShipment.id = :customerId")
    List<CustomerShipmentPosition> getPosition(@Param("customerId") String customerId);


}
