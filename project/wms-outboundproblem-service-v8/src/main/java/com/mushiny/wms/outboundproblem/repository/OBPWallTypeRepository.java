package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPWallType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPWallTypeRepository extends BaseRepository<OBPWallType, String> {

    @Query("select o from OBPWallType o where o.warehouseId = :warehouseId and o.name = :name")
    OBPWallType getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);
}
