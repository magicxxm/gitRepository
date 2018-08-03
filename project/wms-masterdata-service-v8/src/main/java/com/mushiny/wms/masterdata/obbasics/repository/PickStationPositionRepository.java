package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickStationPositionRepository extends BaseRepository<PickStationPosition, String> {
    @Query(" select p from PickStationPosition p where p.pickStation.id = :id ")
    List<PickStationPosition> getByPickStationId(@Param("id") String id);
}
