package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemDataRepository extends BaseRepository<ItemData, String> {

    @Query("select i from ItemData i where i.itemDataGlobalId = :itemDataGlobalId")
    List<ItemData> getByItemDataGlobalId(@Param("itemDataGlobalId") String itemDataGlobalId);


    @Query("select i from ItemData i " +
            " where i.itemNo = :itemNo " +
            " and i.client.id = :clientId " +
            " and i.warehouseId = :warehouseId")
    ItemData getByItemNo(@Param("itemNo") String itemNo,
                         @Param("clientId") String clientId,
                         @Param("warehouseId") String warehouseId);


    @Query("select i from ItemData i where (i.itemNo=:skuNo or i.skuNo = :skuNo) and i.warehouseId = :warehouseId")
    List<ItemData> getBysku(@Param("skuNo") String sku, @Param("warehouseId") String warehouseId);



}
