package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickPackWallTypeRepository extends BaseRepository<PickPackWallType, String> {

    @Query("select b from PickPackWallType b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    PickPackWallType getByName(@Param("warehouse") String warehouse,
                      @Param("name") String name);

}
