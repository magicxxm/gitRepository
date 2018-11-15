package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.StorageLocation;
import com.mushiny.wms.schedule.domin.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnitLoadRepository extends BaseRepository<UnitLoad, String> {

    @Query("select u from UnitLoad u " +
                " where u.storageLocation = :storageLocation and u.warehouseId=:warehouseId and u.entityLock < 2")
    UnitLoad getByStorageLocation(@Param("warehouseId") String warehouseId, @Param("storageLocation") StorageLocation storageLocation);

    @Query("select u from UnitLoad u where u.label = :label")
    UnitLoad getByLabel(@Param("label") String label);


    @Query("select ul from UnitLoad ul where ul.storageLocation=:storageLocation and ul.warehouseId=:warehouseId and ul.entityLock<2")
    UnitLoad findUnitLoadByStorageLocationName(@Param("storageLocation") StorageLocation storageLocation, @Param("warehouseId") String warehouseId);


    @Query("select ul from UnitLoad ul where ul.storageLocation=:storageLocation and ul.warehouseId=:warahouseId and ul.entityLock=0")
    UnitLoad findUnitLoadByStorage(@Param("storageLocation") StorageLocation storageLocation, @Param("warahouseId") String warehouse);

}
