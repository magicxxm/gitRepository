package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.StorageLocation;
import com.mushiny.model.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/2/8.
 */
public interface UnitLoadRepository extends BaseRepository<UnitLoad,String>,UnitLoadRepositoryCustom {

    @Query("select u from UnitLoad u where u.storageLocation = :st and u.warehouseId = :warehouseId and u.entityLock < 2")
    UnitLoad getByStorageLocationAndWarehouse(@Param("st")StorageLocation storageLocation,@Param("warehouseId") String id);

    @Query("select u from UnitLoad u where u.labelId = :label and u.warehouseId = :warehouseId")
    UnitLoad getByLabel(@Param("label") String adviceNo, @Param("warehouseId") String id);
}
