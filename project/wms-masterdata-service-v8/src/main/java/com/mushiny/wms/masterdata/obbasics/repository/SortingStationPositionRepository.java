package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SortingStationPositionRepository extends BaseRepository<SortingStationPosition, String> {
    @Query(" select s from SortingStationPosition s where s.sortingStation.id = :id ")
    List<SortingStationPosition> getBySortingStationId(@Param("id") String id);
}
