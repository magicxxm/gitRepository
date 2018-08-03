package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTakingStationPositionRepository extends BaseRepository<StockTakingStationPosition, String> {
    @Query(" select s from StockTakingStationPosition s where s.stockTakingStation.id = :id ")
    List<StockTakingStationPosition> getByStockTakingStationId(@Param("id") String id);
}
