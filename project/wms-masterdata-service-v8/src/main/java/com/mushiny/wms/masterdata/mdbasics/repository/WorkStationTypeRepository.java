package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkStationTypeRepository extends BaseRepository<WorkStationType, String> {

    @Query(" select a from WorkStationType a " +
            " where a.warehouseId = :warehouse and a.name = :name")
    WorkStationType getByName(@Param("warehouse") String warehouse,
                   @Param("name") String name);
}
