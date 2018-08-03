package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageLocationTypeRepository extends BaseRepository<StorageLocationType, String> {

    @Query(" select s from StorageLocationType s " +
            " where s.warehouseId = :warehouse and s.name =:name")
    StorageLocationType getByName(@Param("warehouse") String warehouse,
                                  @Param("name") String name);

    @Query(" select s.id from StorageLocationType s " +
            " where s.name =:name")
    String getIdByName(@Param("name") String name);
}
