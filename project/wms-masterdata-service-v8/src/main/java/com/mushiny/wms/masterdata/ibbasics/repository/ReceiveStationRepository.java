package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface ReceiveStationRepository extends BaseRepository<ReceiveStation, String> {

    @Query(" select r from ReceiveStation r where r.warehouseId = :warehouse and r.name = :name ")
    ReceiveStation getByName(@Param("warehouse") String warehouse,
                             @Param("name") String name);

    @Query("select r from ReceiveStation  r where r.receivingStationType.id=:id")
    List<ReceiveStation> getByReceiveStationTypeId(@Param("id") String id);

    @Query("select p from ReceiveStation p where p.workStation = :workStation and p.operator=:operatorId")
    ReceiveStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                      @Param("operatorId") String operatorId);

    @Query("select p from ReceiveStation p where p.workStation.id = :workStationId")
    List<ReceiveStation> getByWorkStation(@Param("workStationId") String workStationId);
}
