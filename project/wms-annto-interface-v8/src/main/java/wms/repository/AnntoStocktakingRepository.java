package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.AnntoCustomerOrder;
import wms.domain.AnntoStocktaking;

public interface AnntoStocktakingRepository extends BaseRepository<AnntoStocktaking, String> {


    @Query("select a from AnntoStocktaking a where a.originalCountId=:code and a.warehouseCode = :warehouseCode")
    AnntoStocktaking getByCode(@Param("code") String originalCountId,@Param("warehouseCode")String code);
}
