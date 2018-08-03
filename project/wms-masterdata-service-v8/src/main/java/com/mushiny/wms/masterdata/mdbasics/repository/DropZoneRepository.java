package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.DropZone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DropZoneRepository extends BaseRepository<DropZone, String> {

    @Query(" select d from DropZone d " +
            " where d.warehouseId = :warehouse and d.name = :name")
    DropZone getByName(@Param("warehouse") String warehouse,
                       @Param("name") String name);
}
