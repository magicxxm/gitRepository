package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveDestination;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiveDestinationRepository extends BaseRepository<ReceiveDestination, String> {

    @Query("select r from ReceiveDestination r " +
            " where r.warehouseId = :warehouse and r.name = :name")
    ReceiveDestination getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
