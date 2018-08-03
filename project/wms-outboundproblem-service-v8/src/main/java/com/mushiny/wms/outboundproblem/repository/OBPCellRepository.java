package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPCellRepository extends BaseRepository<OBPCell, String> {

    @Query("select o from OBPCell o where o.warehouseId = :warehouseId and o.name = :name")
    OBPCell getByCellName(@Param("warehouseId") String warehouseId, @Param("name") String name);

    @Query("select o from OBPCell o where o.warehouseId = :warehouseId and o.obpWall.id = :wallId order by o.name desc ")
    List<OBPCell> getByWallId(@Param("warehouseId") String warehouseId, @Param("wallId") String wallId);

}
