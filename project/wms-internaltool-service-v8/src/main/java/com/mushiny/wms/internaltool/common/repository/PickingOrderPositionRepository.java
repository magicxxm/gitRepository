package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.PickingOrderPosition;
import com.mushiny.wms.internaltool.common.domain.StockUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickingOrderPositionRepository extends BaseRepository<PickingOrderPosition, String> {

    @Query("select p from PickingOrderPosition p where p.stockUnit = :stockUnit and p.state < 600")
    List<PickingOrderPosition> getByStockUnit(@Param("stockUnit") StockUnit stockUnit);
}
