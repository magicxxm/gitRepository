package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.ItemDataGlobal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataGlobalRepository extends BaseRepository<ItemDataGlobal, String> {

    @Query(" select i from ItemDataGlobal i where i.skuNo = :itemNo or i.itemNo = :itemNo order by i.name")
    List<ItemDataGlobal> getBySku(@Param("itemNo") String itemNo);

    @Query(" select i from ItemDataGlobal i where i.skuNo = :skuNo order by i.name")
    List<ItemDataGlobal> getBySkuNo(@Param("skuNo") String skuNo);


    @Query(" select i from ItemDataGlobal i where i.itemNo = :itemNo or i.skuNo = :itemNo")
    ItemDataGlobal getByItemDataNo(@Param("itemNo") String itemNo);
}
