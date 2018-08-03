package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RebatchStationTypeRepository extends BaseRepository<RebatchStationType, String> {

    @Query("select p from RebatchStationType p where p.warehouseId = :warehouse and p.name = :name")
    RebatchStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
