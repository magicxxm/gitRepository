package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickStationRepository extends BaseRepository<PickStation, String> {

    @Query("select p from PickStation p where p.warehouseId = :warehouse and p.name = :name")
    PickStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from PickStation p where p.pickStationType.id = :typeId")
    List<PickStation> getByPickStationTypeId(@Param("typeId") String typeId);

    @Query("select p from PickStation p where p.workStation = :workStation and p.operator.id=:operatorId")
    PickStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                         @Param("operatorId") String operatorId);

    @Query("select p from PickStation p where p.workStation.id = :workStationId")
    List<PickStation> getByWorkStation(@Param("workStationId") String workStationId);

//    @Query("select p from PackingStation p where p.packingStationType.id = :typeId")
//    List<PackingStation> getByTypeId(@Param("typeId") String typeId);
}
