package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWallType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPWallTypeRepository extends BaseRepository<OBPWallType, String> {

    @Query("select r from OBPWallType r where r.warehouseId = :warehouse and r.name = :name")
    OBPWallType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
