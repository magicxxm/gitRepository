package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataGlobal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataGlobalRepository extends BaseRepository<ItemDataGlobal, String> {

    @Query(" select i from ItemDataGlobal i where i.itemNo = :itemNo or i.skuNo = :itemNo")
    ItemDataGlobal getByItemDataNo(@Param("itemNo") String itemNo);

    @Query(" select i from ItemDataGlobal i where i.skuNo = :skuNo order by i.name")
    List<ItemDataGlobal> getBySkuNo(@Param("skuNo") String skuNo);

    @Query(" select i.id from ItemDataGlobal i where i.itemNo = :itemNo")
    String getIdByItemDataNo(@Param("itemNo") String itemNo);
}
