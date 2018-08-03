package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;
import com.mushiny.wms.masterdata.obbasics.domain.PickingAreaPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PickingAreaPositionRepository extends BaseRepository<PickingAreaPosition, String> {


    @Query("select p from PickingAreaPosition p where p.zone = :zone")
    PickingAreaPosition getByZone(@Param("zone") Zone zone);
}
