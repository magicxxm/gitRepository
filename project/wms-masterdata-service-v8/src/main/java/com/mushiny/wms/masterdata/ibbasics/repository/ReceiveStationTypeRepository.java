package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiveStationTypeRepository extends BaseRepository<ReceiveStationType, String> {

    @Query("select r from ReceiveStationType r where r.warehouseId = :warehouse and r.name = :name")
    ReceiveStationType getByName(@Param("warehouse") String warehouse,
                                 @Param("name") String name);
}
