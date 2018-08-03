package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RebatchStationPositionRepository extends BaseRepository<RebatchStationPosition, String> {
    @Query(" select r from RebatchStationPosition r where r.rebatchStation.id = :id ")
    List<RebatchStationPosition> getByRebatchStationId(@Param("id") String id);
}
