package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPStationTypePositionRepository extends BaseRepository<OBPStationTypePosition, String> {

    @Query(" select b from OBPStationTypePosition b " +
            " where b.obpStationType = :obpStationType  order by b.positionIndex")
    List<OBPStationTypePosition> getByOBPStationType(@Param("obpStationType") OBPStationType obpStationType);

}
