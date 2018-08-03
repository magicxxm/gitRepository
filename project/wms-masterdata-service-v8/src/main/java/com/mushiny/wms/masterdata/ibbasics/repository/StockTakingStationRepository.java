package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTakingStationRepository extends BaseRepository<StockTakingStation, String> {

    @Query("select p from StockTakingStation p where p.warehouseId = :warehouse and p.name = :name")
    StockTakingStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from StockTakingStation p where p.stockTakingStationType.id=:typeId")
    List<StockTakingStation> getByStockTakingStationTypeId(@Param("typeId") String typeId);

    @Query("select p from StockTakingStation p where p.workStation = :workStation and p.operatorId.id=:operatorId")
    StockTakingStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                      @Param("operatorId") String operatorId);

    @Query("select p from StockTakingStation p where p.workStation.id= :workStationId")
    List<StockTakingStation> getByWorkStation(@Param("workStationId") String workStationId);

}
