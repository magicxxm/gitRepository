package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPStationType;
import com.mushiny.wms.outboundproblem.domain.OBPStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPStationTypePositionRepository extends BaseRepository<OBPStationTypePosition, String> {

    @Query("select o from OBPStationTypePosition o " +
            " where o.obpStationType = :obpStationType")
    List<OBPStationTypePosition> getPositions(
            @Param("obpStationType") OBPStationType obpStationType);
}
