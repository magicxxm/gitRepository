package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/2/8.
 */
public interface StorageLocationRepository extends BaseRepository<StorageLocation,String> {

    @Query("select s from StorageLocation s where s.name = :name and s.warehouseId = :warehouseId")
    StorageLocation getByNameAndWarehouseId(@Param("name") String storageLocationName, @Param("warehouseId")String warehouseId);

}
