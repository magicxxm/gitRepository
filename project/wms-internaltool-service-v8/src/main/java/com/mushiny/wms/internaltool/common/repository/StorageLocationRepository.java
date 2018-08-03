package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageLocationRepository extends BaseRepository<StorageLocation, String> {

    @Query(" select s from StorageLocation s " +
            " where s.warehouseId = :warehouse and s.name =:name")
    StorageLocation getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);
}
