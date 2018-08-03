package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallType;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReBinWallTypeRepository extends BaseRepository<ReBinWallType, String> {

    @Query("select r from ReBinWallType r where r.warehouseId = :warehouse and r.name = :name")
    ReBinWallType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
