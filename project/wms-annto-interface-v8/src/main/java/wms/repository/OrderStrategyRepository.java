package wms.repository;

import wms.common.respository.BaseRepository;
import wms.domain.common.OrderStrategy;

public interface OrderStrategyRepository extends BaseRepository<OrderStrategy, String> {

//    @Query(" select i from OrderStrategy i where i.orderNo =:orderNo and i.clientId = :clientId and i.entityLock = 0")
//    OrderStrategy OrderStrategy(@Param("clientId") String clientId,
//                               @Param("orderNo") String orderNo);
}
