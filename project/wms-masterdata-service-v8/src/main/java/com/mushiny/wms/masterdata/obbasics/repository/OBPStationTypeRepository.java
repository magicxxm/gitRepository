package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPStationTypeRepository extends BaseRepository<OBPStationType, String> {

    @Query("select p from PackingStation p where p.warehouseId = :warehouse and p.name = :name")
    OBPStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);

}
