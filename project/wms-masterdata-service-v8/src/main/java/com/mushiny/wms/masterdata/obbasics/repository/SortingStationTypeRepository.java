package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SortingStationTypeRepository extends BaseRepository<SortingStationType, String> {

    @Query("select p from SortingStationType p where p.warehouseId = :warehouse and p.name = :name")
    SortingStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
