package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationType;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SortingStationTypePositionRepository extends BaseRepository<SortingStationTypePosition, String> {

    @Query(" select b from SortingStationTypePosition b " +
            " where b.sortingStationType = :sortingStationType  order by b.positionIndex")
    List<SortingStationTypePosition> getBySortingStationType(@Param("sortingStationType") SortingStationType sortingStationType);
}
