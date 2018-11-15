package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.SystemStocktaking;

/**
 * Created by 123 on 2017/8/30.
 */
public interface SystemStocktakingRepository extends BaseRepository<SystemStocktaking,String> {

    @Query("select s from SystemStocktaking s where s.stockNo = :stockNo")
    SystemStocktaking getByStockNo(@Param("stockNo")String stockNo);
}
