package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.GoodsOutDoor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoodsOutDoorRepository extends BaseRepository<GoodsOutDoor, String> {

    @Query("select b from GoodsOutDoor b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    GoodsOutDoor getByName(@Param("warehouse") String warehouse,
                           @Param("name") String name);

}
