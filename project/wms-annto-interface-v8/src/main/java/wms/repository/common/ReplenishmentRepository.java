package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.Replenishment;

public interface ReplenishmentRepository extends BaseRepository<Replenishment, String> {

    @Query(" select i from Replenishment i where i.ordercode =:orderCode and i.warehouseId = :warehouseId")
    Replenishment getByOrderCode(@Param("warehouseId") String warehouseId,
                            @Param("orderCode") String orderCode);
}
