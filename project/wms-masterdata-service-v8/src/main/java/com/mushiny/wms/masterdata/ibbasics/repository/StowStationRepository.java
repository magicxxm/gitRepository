package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StowStationRepository extends BaseRepository<StowStation, String> {

    @Query(" select a from StowStation a " +
            " where a.warehouseId = :warehouse and a.name = :name")
    StowStation getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);

    @Query("select a from StowStation a where a.type.id=:id")
    List<StowStation> getByStowStationTypeId(@Param("id") String id);

    @Query("select p from StowStation p where p.workstation = :workStation and p.operator.id=:operatorId")
    StowStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                  @Param("operatorId") String operatorId);

    @Query("select p from StowStation p where p.workstation.id = :workStationId ")
    List<StowStation> getByWorkStation(@Param("workStationId") String workStationId);
}
