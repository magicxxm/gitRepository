package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageLocationRepository extends BaseRepository<StorageLocation, String> {

    @Query(" select s from StorageLocation s " +
            " where s.warehouseId = :warehouseId and s.name =:name and s.storageLocationType.storageType = 'CONTAINER' " +
            "and s.storageLocationType.inventoryState = :inventoryState")
    StorageLocation getByName(@Param("warehouseId") String warehouseId,
                              @Param("name") String name,
                              @Param("inventoryState") String inventoryState);

    @Query(" select s from StorageLocation s where s.warehouseId = :warehouseId and s.name =:name ")
    StorageLocation getAllStoragetypeByName(@Param("warehouseId") String warehouseId,
                              @Param("name") String name);

    @Query(" select max(s.orderIndex) from StorageLocation s ")
    Integer getMaxOrderIndex();

}
