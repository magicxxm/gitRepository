package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PodTypeRepository extends BaseRepository<PodType, String> {

    @Query(" select b from PodType b " +
            " where b.warehouseId = :warehouseId and b.name = :name")
    PodType getByName(@Param("warehouseId") String warehouseId,
                      @Param("name") String name);
}
