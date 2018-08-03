package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBPStationPositionRepository extends BaseRepository<IBPStationPosition, String> {
    @Query(" select i from IBPStationPosition i where i.ibpStation.id = :id ")
    List<IBPStationPosition> getByIbpStationId(@Param("id") String id);
}
