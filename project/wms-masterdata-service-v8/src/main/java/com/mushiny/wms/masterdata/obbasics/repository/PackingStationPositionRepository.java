package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackingStationPositionRepository extends BaseRepository<PackingStationPosition, String> {
    @Query(" select p from PackingStationPosition p where p.packingStation.id = :id ")
    List<PackingStationPosition> getByPackingStationId(@Param("id") String id);
}
