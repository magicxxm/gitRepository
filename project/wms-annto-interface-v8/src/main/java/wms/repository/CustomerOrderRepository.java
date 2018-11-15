package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.CustomerOrder;

public interface CustomerOrderRepository extends BaseRepository<CustomerOrder, String> {

    @Query(" select i from CustomerOrder i where i.orderNo =:orderNo and i.customerNo = :customerNo and i.entityLock = 0")
    CustomerOrder getByOrderNo(@Param("customerNo") String customerNo,
                                @Param("orderNo") String orderNo);

    @Query(" select i from CustomerOrder i where i.orderNo =:orderNo and i.warehouseId = :warehouseId")
    CustomerOrder getByOrderNoAAndWarehouseId(@Param("orderNo") String orderNo,
                               @Param("warehouseId") String warehouseId);
}
