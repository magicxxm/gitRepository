package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StowStationTypeRepository extends BaseRepository<StowStationType, String> {

    @Query(" select a from StowStationType a " +
            " where a.warehouseId = :warehouse and a.name = :name")
    StowStationType getByName(@Param("warehouse") String warehouse,
                               @Param("name") String name);
}
