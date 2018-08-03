package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.AdviceRequestPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdviceRequestPositionRepository extends BaseRepository<AdviceRequestPosition, String> {

    @Query("select ap from AdviceRequestPosition ap " +
            " left join ap.itemData i " +
            " where ap.warehouseId = :warehouseId " +
            " and (i.itemNo = :sku or i.skuNo = :sku)")
    List<AdviceRequestPosition> getBySku(@Param("warehouseId") String warehouseId,
                                         @Param("sku") String sku);
}
