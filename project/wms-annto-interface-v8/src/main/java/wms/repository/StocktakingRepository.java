package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.Stocktaking;

public interface StocktakingRepository extends BaseRepository<Stocktaking, String> {

    @Query(" select i from Stocktaking i where i.stocktakingNo =:stocktakingNo and i.warehouseId = :warehouseId")
    Stocktaking getByTakingNo(@Param("warehouseId") String warehouseId,
                                 @Param("stocktakingNo") String stocktakingNo);

}
