package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataRepository extends BaseRepository<ItemData, String> {

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and i.skuNo = :skuNo")
    List<ItemData> getBySkuNo(@Param("warehouse") String warehouse,
                               @Param("skuNo") String skuNo);

    @Query(" select i from ItemData i " +
            " where i.itemNo = :itemNo " +
            " and i.clientId = :clientId " +
            " and i.warehouseId = :warehouseId ")
    ItemData getByItemNoes(@Param("itemNo") String itemNo,
                         @Param("clientId") String clientId,
                         @Param("warehouseId") String warehouseId);

    @Query("select i from ItemData i " +
            " where i.itemDataGlobalId = :itemDataGlobalId")
    List<ItemData> getByItemDataGlobal(@Param("itemDataGlobalId") String itemDataGlobalId);

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and (i.skuNo = :itemNo or i.itemNo=:itemNo)")
    List<ItemData> getByItemNoSkuNo(@Param("warehouse") String warehouse,
                              @Param("itemNo") String itemNo);

    @Query(" select i from ItemData i " +
            " where (i.itemNo = :itemNo or i.skuNo=:itemNo)" +
            " and i.clientId = :clientId " +
            " and i.warehouseId = :warehouseId ")
    ItemData getByItemNo(@Param("warehouseId") String warehouseId,
                         @Param("clientId") String clientId,
                         @Param("itemNo") String itemNo);
}
