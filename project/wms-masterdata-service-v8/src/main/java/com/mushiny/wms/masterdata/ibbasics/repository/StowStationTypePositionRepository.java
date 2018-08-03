package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationTypePosition;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;
import com.mushiny.wms.masterdata.mdbasics.domain.PodTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StowStationTypePositionRepository extends BaseRepository<StowStationTypePosition, String> {

//    @Query(" select a from StowStationType a " +
//            " where a.warehouseId = :warehouse and a.name = :name")
//    StowStationTypePosition getByName(@Param("warehouse") String warehouse,
//                              @Param("name") String name);

    @Query(" select b from StowStationTypePosition b " +
            " where b.stowStationType = :stowStationType  order by b.positionIndex")
    List<StowStationTypePosition> getByStowStationType(@Param("stowStationType") StowStationType stowStationType);

    @Query(" select b from StowStationTypePosition b " +
            " where b.stowStationType = :stowStationType  order by b.positionIndex")
    StowStationTypePosition getByStationType(@Param("stowStationType") StowStationType stowStationType);
}
