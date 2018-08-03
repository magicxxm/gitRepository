package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockTakingStationTypeRepository extends BaseRepository<StockTakingStationType, String> {

    @Query("select p from StockTakingStationType p where p.warehouseId = :warehouse and p.name = :name")
    StockTakingStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);

}
