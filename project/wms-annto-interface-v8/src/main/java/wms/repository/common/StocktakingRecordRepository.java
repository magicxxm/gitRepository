package wms.repository.common;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.StocktakingOrder;
import wms.domain.common.StocktakingRecord;

import java.util.List;

public interface StocktakingRecordRepository extends BaseRepository<StocktakingRecord, String> {

    @Query("select s from StocktakingRecord s where s.stocktakingOrder=:s and s.clientNo = :clientNo")
    List<StocktakingRecord> getByStocktakingOrder(@Param("s")StocktakingOrder s,@Param("clientNo")String clientNo);
}
