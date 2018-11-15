package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerOrder;
import com.mushiny.wms.schedule.domin.CustomerShipment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomerShipmentRepository extends BaseRepository<CustomerShipment,String> {

    @Query("select c from CustomerShipment c where c.warehouseId=:warehouseId and c.shipmentNo=:shipmentNo")
    CustomerShipment getByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query("select c from CustomerShipment c where c.shipmentNo=:shipmentNo")
    CustomerShipment getByShipmentNo(@Param("shipmentNo") String shipmentNo);

    @Query("select c from CustomerShipment c where c.deliveryDate=:deliverTime and c.sortCode=:sortCode")
    List<CustomerShipment> getByDeliveryDateAndSortCode(@Param("sortCode") String sortCode, @Param("deliverTime") LocalDateTime deliverTime);

    @Query("select c from CustomerShipment c where c.deliveryDate=:deliverTime and c.sortCode=:sortCode and c.state=:state")
    List<CustomerShipment> getByDeliveryDateAndSortCodeAndState(@Param("sortCode") String sortCode, @Param("deliverTime") LocalDateTime deliverTime, @Param("state") int state);

    @Query("select ship from CustomerShipment ship where ship.customerOrder=:order")
    List<CustomerShipment> getByCustomerOrder(@Param("order") CustomerOrder order);

    @Query("select ship from CustomerShipment ship where ship.sortCode like :search")
    List<CustomerShipment> getByCustDetail(@Param("search") String search);

    @Query("select ship.state from CustomerShipment ship where ship.deliveryDate=:deliverTime and ship.sortCode=:sortCode")
    List<Integer> getStateByDeliveryDateAndSortCode(@Param("sortCode") String sortCode, @Param("deliverTime") LocalDateTime deliverTime);

    @Query("select ship from CustomerShipment ship where ship.sortCode=:sortCode and ship.state>660 and ship.state<690")
    List<CustomerShipment> getBySortCodeAndNotOut(@Param("sortCode") String sortCode);

    @Query("select ship from CustomerShipment ship where ship.sortCode=:sortCode and ship.state=:state")
    List<CustomerShipment> getBySortCodeAndState(@Param("sortCode") String sortCode, @Param("state") int state);

    @Query("select coalesce(count(ship.shipmentNo),0) from CustomerShipment ship where ship.sortCode=:sortCode and ship.deliveryDate=:deliverTime")
    long countBySortCode(@Param("sortCode") String sortCode, @Param("deliverTime") LocalDate deliverTime);

    @Query("select distinct ship.deliveryDate from CustomerShipment ship where ship.sortCode=:sortCode and ship.state=:state")
    List<LocalDateTime> countTimePointBySortCodeAndState(@Param("sortCode") String sortCode, @Param("state") int state);

    @Query("select coalesce(count(ship),0) from CustomerShipment ship where ship.sortCode=:sortCode and ship.state=:state")
    long countShipmentBySortCodeAndState(@Param("sortCode") String sortCode, @Param("state") int state);

    @Query("select coalesce(count(ship),0) from CustomerShipment ship where ship.deliveryDate=:deliverTime and ship.sortCode=:sortCode and ship.state=:state")
    long countByDeliveryDateAndSortCodeAndState(@Param("deliverTime") LocalDateTime deliverTime, @Param("sortCode") String sortCode, @Param("state") int state);

    @Query("select coalesce(count(ship),0) from CustomerShipment ship where ship.deliveryDate=:deliverTime and ship.sortCode=:sortCode")
    long countByDeliveryDateAndSortCode(@Param("deliverTime") LocalDateTime deliverTime, @Param("sortCode") String sortCode);

    @Query("select c from CustomerShipment c where c.state < :state")
    List<CustomerShipment> getByState(@Param("state") int processable);
}
