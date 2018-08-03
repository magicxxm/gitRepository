package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationType;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackingStationTypePositionRepository extends BaseRepository<PackingStationTypePosition, String> {

    @Query(" select b from PackingStationTypePosition b " +
            " where b.packingStationType = :packingStationType  order by b.positionIndex")
    List<PackingStationTypePosition> getByStowStationType(@Param("packingStationType") PackingStationType packingStationType);

}
