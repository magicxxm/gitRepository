package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.StorageLocation;
import wms.domain.common.UnitLoad;

import java.util.List;

/**
 * Created by 123 on 2017/4/29.
 */
public interface UnitLoadRepository extends BaseRepository<UnitLoad,String> {

    @Query("select ul from UnitLoad ul where ul.storageLocation=:storageLocation and ul.warehouseId=:warahouseId and ul.entityLock<2")
    List<UnitLoad> findUnitLoadByStorageLocation(@Param("storageLocation") StorageLocation storageLocation, @Param("warahouseId") String warehouseId);

    @Query("select ul from UnitLoad ul where ul.storageLocation=:storageLocation and ul.warehouseId=:warahouseId and ul.entityLock<2")
    UnitLoad findUnitLoadByStorageLocationName(@Param("storageLocation") StorageLocation storageLocation, @Param("warahouseId") String warehouseId);

    @Query("select u from UnitLoad u where u.label=:label")
    UnitLoad getByLabel(@Param("label") String label);

    @Query("select ul from UnitLoad ul where ul.storageLocation=:storageLocation and ul.warehouseId=:warahouseId and ul.entityLock=0")
    UnitLoad findUnitLoadByStorage(@Param("storageLocation") StorageLocation storageLocation, @Param("warahouseId") String warehouse);

    @Query("select u from UnitLoad u where u.storageLocation=:storageLocation and u.entityLock <> 2")
    UnitLoad getByStorageLocation(@Param("storageLocation") StorageLocation s);

}
