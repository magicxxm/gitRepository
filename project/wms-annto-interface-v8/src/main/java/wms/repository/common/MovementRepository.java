package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.Movement;

public interface MovementRepository extends BaseRepository<Movement, String> {

    @Query(" select i from Movement i where i.ordercode =:orderCode and i.warehouseId = :warehouseId")
    Movement getByOrderCode(@Param("warehouseId") String warehouseId,
                            @Param("orderCode") String orderCode);
}
