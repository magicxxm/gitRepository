package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.TimeConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeConfigRepository extends BaseRepository<TimeConfig, String> {
    @Query(" select i from TimeConfig i where i.warehouseId= :warehouseId ")
    TimeConfig getByWarehouseId(@Param("warehouseId") String warehouseId);
}
