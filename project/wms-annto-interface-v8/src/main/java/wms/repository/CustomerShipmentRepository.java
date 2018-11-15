package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.CustomerOrder;
import wms.domain.common.CustomerShipment;

import java.util.List;


/**
 * Created by 123 on 2017/5/16.
 */
public interface CustomerShipmentRepository extends BaseRepository<CustomerShipment,String> {

    @Query("select c from CustomerShipment c where c.customerNo=:orderNo and c.customerOrder=:order")
    List<CustomerShipment> getByCustomerNo(@Param("orderNo") String orderNo,@Param("order")CustomerOrder order);

    @Query("select c from CustomerShipment c where c.shipmentNo=:shipmentNo")
    CustomerShipment getByShipmentNo(@Param("shipmentNo")String shipmentNo);

    @Query("select s from CustomerShipment s where s.customerOrder = :order")
    List<CustomerShipment> getByCustomer(@Param("order") CustomerOrder order);
}
