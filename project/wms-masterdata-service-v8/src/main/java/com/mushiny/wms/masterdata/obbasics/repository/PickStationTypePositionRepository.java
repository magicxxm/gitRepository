package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationType;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickStationTypePositionRepository extends BaseRepository<PickStationTypePosition, String> {

    @Query(" select b from PickStationTypePosition b " +
            " where b.pickStationType = :pickStationType  order by b.positionIndex")
    List<PickStationTypePosition> getByPickStationType(@Param("pickStationType") PickStationType pickStationType);

}
