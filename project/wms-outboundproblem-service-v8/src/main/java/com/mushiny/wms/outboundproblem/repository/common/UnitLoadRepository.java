package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.common.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitLoadRepository extends BaseRepository<UnitLoad, String> {

    @Query("select u from UnitLoad u where u.label = :label")
    UnitLoad getByLabel(@Param("label") String label);

    @Query("select u from UnitLoad u where u.storageLocation = :storageLocation")
    UnitLoad getOneByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query("select u from UnitLoad u " +
                " where u.storageLocation = :storageLocation and u.entityLock < 2")
    List<UnitLoad> getByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query("select u from UnitLoad u " +
            " left join u.shipments s " +
            " where s.shipmentNo = :shipmentNo")
    UnitLoad getByShipmentNo(@Param("shipmentNo") String shipmentNo);

    @Query("select u from UnitLoad u where u.storageLocation = :storageLocation and u.entityLock = 1")
    UnitLoad getByDestination(@Param("storageLocation") StorageLocation storageLocation);

    @Query("select u from UnitLoad u where u.storageLocation = :storageLocation and u.entityLock > 1")
    UnitLoad getByStorageLocationStowLess(@Param("storageLocation") StorageLocation storageLocation);

    //上货
    @Query("select u from UnitLoad u " +
            " where u.storageLocation = :storageLocation and u.entityLock = 0 ")
    UnitLoad getByStorageLocationLimit(@Param("storageLocation") StorageLocation storageLocation);

    // 收货容器之间的移货
    @Query("select u from UnitLoad u " +
            " where u.storageLocation = :storageLocation and u.entityLock = 1 ")
    UnitLoad getByStorageLocationAndEntityLock(@Param("storageLocation") StorageLocation storageLocation);

    //移货
    @Query("select u from UnitLoad u " +
            " where u.storageLocation = :storageLocation and u.entityLock < 2 ")
    UnitLoad movingGetByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query("select s from UnitLoad u " +
            " left join u.shipments s " +
            " where u.storageLocation = :storageLocation")
    CustomerShipment getByLocation(@Param("storageLocation") StorageLocation storageLocation);
}
