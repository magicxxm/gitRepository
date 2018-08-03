package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickStationTypeRepository extends BaseRepository<PickStationType, String> {

    @Query("select p from PackingStation p where p.warehouseId = :warehouse and p.name = :name")
    PickStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);

}
