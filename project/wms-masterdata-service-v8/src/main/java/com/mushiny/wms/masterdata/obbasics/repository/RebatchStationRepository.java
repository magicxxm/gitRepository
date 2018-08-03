package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStation;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RebatchStationRepository extends BaseRepository<RebatchStation, String> {

    @Query("select p from RebatchStation p where p.warehouseId = :warehouse and p.name = :name")
    RebatchStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from RebatchStation  p where p.rebatchStationType.id=:id")
    List<RebatchStation> getByRebatchStationTypeId(@Param("id") String id);

    @Query("select p from RebatchStation p where p.workStation = :workStation and p.operator.id=:operatorId")
    RebatchStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                    @Param("operatorId") String operatorId);

}
