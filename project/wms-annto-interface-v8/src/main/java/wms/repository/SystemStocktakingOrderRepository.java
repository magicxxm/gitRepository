package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.SystemStocktakingOrder;
import wms.domain.SystemStocktakingPosition;

import java.util.List;

/**
 * Created by 123 on 2017/8/30.
 */
public interface SystemStocktakingOrderRepository extends BaseRepository<SystemStocktakingOrder,String> {

    @Query("select s from SystemStocktakingOrder s where s.position = :position order by s.stockIndex")
    List<SystemStocktakingOrder> getAll(@Param("position") SystemStocktakingPosition position);
}
