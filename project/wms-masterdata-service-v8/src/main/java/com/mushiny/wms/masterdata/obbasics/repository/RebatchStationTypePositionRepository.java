package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RebatchStationTypePositionRepository extends BaseRepository<RebatchStationTypePosition, String> {

    @Query(" select b from RebatchStationTypePosition b " +
            " where b.rebatchStationType = :rebatchStationType  order by b.positionIndex")
    List<RebatchStationTypePosition> getByRebatchStationType(@Param("rebatchStationType") RebatchStationType rebatchStationType);
}
