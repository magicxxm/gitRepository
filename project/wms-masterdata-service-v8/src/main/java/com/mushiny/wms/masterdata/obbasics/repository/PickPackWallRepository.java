package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickPackWallRepository extends BaseRepository<PickPackWall, String> {

    @Query("select b from PickPackWall b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    PickPackWall getByName(@Param("warehouse") String warehouse,
                               @Param("name") String name);

    @Query("select b from PickPackWall b where b.pickPackWallType.id=:id")
    List<PickPackWall> getByPickPackTypeId(@Param("id") String id);

}
