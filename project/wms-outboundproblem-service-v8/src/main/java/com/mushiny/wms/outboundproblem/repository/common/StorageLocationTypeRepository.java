package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocationType;
import org.springframework.data.jpa.repository.Query;

public interface StorageLocationTypeRepository extends BaseRepository<StorageLocationType, String> {

    @Query(" select s from StorageLocationType s where s.inventoryState = 'INVENTORY' and s.storageType = 'CONTAINER' " )
    StorageLocationType getInventoryContainer();
}
