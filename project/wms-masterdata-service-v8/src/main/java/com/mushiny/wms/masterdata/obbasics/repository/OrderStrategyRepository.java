package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OrderStrategy;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderStrategyRepository extends BaseRepository<OrderStrategy, String> {

    @Query("select o from OrderStrategy o " +
            " where o.warehouseId = :warehouse and o.clientId = :client and o.name = :name")
    OrderStrategy getByName(@Param("warehouse") String warehouse,
                            @Param("client") String client,
                            @Param("name") String name);
}
