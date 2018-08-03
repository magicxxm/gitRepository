package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBPStationRepository extends BaseRepository<IBPStation, String> {

    @Query("select p from IBPStation p where p.warehouseId = :warehouse and p.name = :name")
    IBPStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from IBPStation p where p.ibpStationType.id = :typeId")
    List<IBPStation> getByTypeId(@Param("typeId") String typeId);

    @Query("select p from IBPStation p where p.workStation = :workStation and p.operatorId.id=:operatorId")
    IBPStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                          @Param("operatorId") String operatorId);

    @Query("select p from IBPStation p where p.workStation.id = :workStationId ")
    List<IBPStation> getByWorkStation(@Param("workStationId") String workStationId);
}
