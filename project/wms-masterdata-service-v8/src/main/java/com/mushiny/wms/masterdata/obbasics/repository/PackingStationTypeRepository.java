package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationType;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackingStationTypeRepository extends BaseRepository<PackingStationType, String> {

    @Query("select p from PackingStationType p where p.warehouseId = :warehouse and p.name = :name")
    PackingStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
