package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.StorageLocation;
import com.mushiny.wms.internaltool.common.domain.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitLoadRepository extends BaseRepository<UnitLoad, String> {


    @Query("select u from UnitLoad u where u.label = :label")
    UnitLoad getByLabel(@Param("label") String label);

    @Query("select u from UnitLoad u " +
            " where u.storageLocation = :storageLocation and u.entityLock in (0,1)")
    UnitLoad getByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query("select u from UnitLoad u " +
            " where u.storageLocation = :storageLocation and u.entityLock in (0,1)")
    List<UnitLoad> getByStorageLocationName(@Param("storageLocation") StorageLocation storageLocation);
}
