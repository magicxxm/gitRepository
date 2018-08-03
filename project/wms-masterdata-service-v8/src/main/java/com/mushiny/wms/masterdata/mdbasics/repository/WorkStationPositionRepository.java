package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkStationPositionRepository extends BaseRepository<WorkStationPosition, String> {

    @Query(" select a from WorkStationPosition a " +
            " where a.workStation = :workStation and a.positionIndex = :positionIndex")
    WorkStationPosition getByWorkStationIdAndPositionIndex(@Param("workStation") WorkStation workStation,
                                                           @Param("positionIndex") int positionIndex);
}
