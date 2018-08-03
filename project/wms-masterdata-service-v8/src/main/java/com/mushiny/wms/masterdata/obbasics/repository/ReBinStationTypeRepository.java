package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStationType;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReBinStationTypeRepository extends BaseRepository<ReBinStationType, String> {

    @Query("select r from ReBinStationType r where r.warehouseId = :warehouse and r.name = :name")
    ReBinStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);
}
