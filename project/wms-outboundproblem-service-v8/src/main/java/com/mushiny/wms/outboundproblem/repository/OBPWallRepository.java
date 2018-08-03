package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPWallRepository extends BaseRepository<OBPWall, String> {

    @Query("select o from OBPWall o where o.warehouseId = :warehouseId and o.name = :name")
    OBPWall getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);

//    @Query("select o from OBPWall o where o.obpWallType.id = :obpWallTypeId")
//    List<OBPWall> getByReBinWallTypeId(@Param("obpWallTypeId") String obpWallTypeId);
}
