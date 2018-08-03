package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplenishStrategyRepository extends BaseRepository<ReplenishStrategy, String> {

    @Query("select r from ReplenishStrategy r where r.warehouseId = :warehouse and r.clientId = :client")
    ReplenishStrategy getByClient(@Param("warehouse") String warehouse,
                                  @Param("client") String client);
}
