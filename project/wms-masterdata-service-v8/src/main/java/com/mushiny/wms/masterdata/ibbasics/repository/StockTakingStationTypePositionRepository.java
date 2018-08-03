package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTakingStationTypePositionRepository extends BaseRepository<StockTakingStationTypePosition, String> {

    @Query(" select b from StockTakingStationTypePosition b " +
            " where b.stockTakingStationType = :ibpStationType  order by b.positionIndex")
    List<StockTakingStationTypePosition> getByStockTakingStationType(@Param("ibpStationType") StockTakingStationType stockTakingStationType);

}
