package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPStationTypeRepository extends BaseRepository<OBPStationType, String> {

//    long countByOBPStationType(OBPStationType obpStationType);

    @Query("select o from OBPStationType o where o.warehouseId = :warehouseId and o.name = :name")
    OBPStationType getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);
}
