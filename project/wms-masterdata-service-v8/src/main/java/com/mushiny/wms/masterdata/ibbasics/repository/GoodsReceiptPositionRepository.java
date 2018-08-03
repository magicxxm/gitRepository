package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceiptPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface GoodsReceiptPositionRepository extends BaseRepository<GoodsReceiptPosition, String> {

    @Query(" select coalesce(sum (g.amount),0) from GoodsReceiptPosition g " +
            " where g.goodsReceipt = :goodsReceipt ")
    BigDecimal sumByGoodsReceipt(@Param("goodsReceipt") GoodsReceipt goodsReceipt);

    @Query(" select coalesce(sum (g.amount),0) from GoodsReceiptPosition g " +
            " where g.goodsReceipt = :goodsReceipt and g.itemData = :itemNo")
    BigDecimal sumByItemNo(@Param("goodsReceipt") GoodsReceipt goodsReceipt,
                           @Param("itemNo") String itemNo);
}
