package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackingStationRepository extends BaseRepository<PackingStation, String> {

    @Query("select p from PackingStation p where p.warehouseId = :warehouse and p.name = :name")
    PackingStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from PackingStation p where p.packingStationType.id = :typeId")
    List<PackingStation> getByTypeId(@Param("typeId") String typeId);

    @Query("select p from PackingStation p where p.workStation = :workStation and p.operator.id=:operatorId")
    PackingStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                      @Param("operatorId") String operatorId);
}
