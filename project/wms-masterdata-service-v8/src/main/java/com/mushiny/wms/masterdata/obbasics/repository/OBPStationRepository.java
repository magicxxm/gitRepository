package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPStationRepository extends BaseRepository<OBPStation, String> {

    @Query("select p from OBPStation p where p.warehouseId = :warehouse and p.name = :name")
    OBPStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from OBPStation p where p.obpStationType.id = :typeId")
    List<OBPStation> getByTypeId(@Param("typeId") String typeId);

    @Query("select p from OBPStation p where p.workStation = :workStation and p.operatorId.id=:operatorId")
    OBPStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                  @Param("operatorId") String operatorId);

    @Query("select p from OBPStation p where p.workStation.id = :workStationId ")
    List<OBPStation> getByWorkStation(@Param("workStationId") String  workStationId);
}
