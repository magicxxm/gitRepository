package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AreaRepository extends BaseRepository<Area, String> {

    @Query(" select a from Area a " +
            " where a.warehouseId = :warehouse and a.clientId = :client and a.name = :name")
    Area getByName(@Param("warehouse") String warehouse,
                   @Param("client") String client,
                   @Param("name") String name);
}
