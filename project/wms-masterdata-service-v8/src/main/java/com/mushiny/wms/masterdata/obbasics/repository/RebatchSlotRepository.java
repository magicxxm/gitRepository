package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchSlot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RebatchSlotRepository extends BaseRepository<RebatchSlot, String> {

    @Query("select r from RebatchSlot r where r.warehouseId = :warehouse and r.name = :name")
    RebatchSlot getByName(@Param("warehouse") String warehouseId, @Param("name") String name);

}
