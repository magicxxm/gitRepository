package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface SortingStationRepository extends BaseRepository<SortingStation, String> {

    @Query("select p from SortingStation p where p.warehouseId = :warehouse and p.name = :name")
    SortingStation getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select p from SortingStation p where p.sortingStationType.id=:id")
    List<SortingStation> getBySortingStationTypeId(@Param("id") String id);

    @Query("select p from SortingStation p where p.workStation = :workStation and p.operator.id=:operatorId")
    SortingStation getByWorkStationId(@Param("workStation") WorkStation workStation,
                                   @Param("operatorId") String operatorId);
}
