package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.AdviceRequest;
import com.mushiny.wms.internaltool.common.domain.GoodsReceiptPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GoodsReceiptPositionRepository extends BaseRepository<GoodsReceiptPosition, String> {

    @Query(" select coalesce(sum (g.amount),0) from GoodsReceiptPosition g " +
            " left join g.goodsReceipt gr " +
            " where gr.relatedAdvice = :adviceRequest and g.itemData.itemNo = :itemNo")
    BigDecimal sumByDnAndItemNo(@Param("adviceRequest") AdviceRequest adviceRequest,
                                @Param("itemNo") String itemNo);

    @Query("select g from GoodsReceiptPosition g " +
            " left join g.itemData i " +
            " where g.warehouseId = :warehouseId " +
            " and (i.itemNo = :sku or i.skuNo = :sku)")
    List<GoodsReceiptPosition> getBySku(@Param("warehouseId") String warehouseId,
                                        @Param("sku") String sku);
}
