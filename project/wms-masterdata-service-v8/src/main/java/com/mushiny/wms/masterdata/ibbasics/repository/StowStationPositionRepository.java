package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StowStationPositionRepository extends BaseRepository<StowStationPosition, String> {

    //    @Query(" select a from StowStation a " +
//            " where a.warehouseId = :warehouse and a.name = :name")
//    StowStation getByName(@Param("warehouse") String warehouse,
//                          @Param("name") String name);
    @Query(" select b from StowStationPosition b " +
            " where b.stowStation.id = :id")
    List<StowStationPosition> getByStowStationId(@Param("id") String id);
}
