package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitLoadRepository extends BaseRepository<UnitLoad, String> {

    @Query("select u from UnitLoad u where u.label = :label")
    UnitLoad getByLabel(@Param("label") String label);

    @Query("select u from UnitLoad u where u.storageLocation = :storageLocation and u.entityLock<>2")
    UnitLoad getByEntityLock(@Param("storageLocation")StorageLocation storageLocation);

    //
//    @Query("select u from UnitLoad u where u.container = :container")
//    UnitLoad getByContainer(@Param("container")Container container);
//
//    @Query("select u from UnitLoad u where u.storageLocation.id = :id")
//    List<UnitLoad> getByStorageLocation(@Param("id") String id);
}
