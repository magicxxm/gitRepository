package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPStationPositionRepository extends BaseRepository<OBPStationPosition, String> {
    @Query(" select o from OBPStationPosition o where o.obpStation.id = :id ")
    List<OBPStationPosition> getByOBPStationId(@Param("id") String id);
}
