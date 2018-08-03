package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataGlobalSku;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemSkuRepository extends BaseRepository<ItemDataGlobalSku, String> {
    @Query("select s from ItemDataGlobalSku s where s.skuNo=:skuNo")
    ItemDataGlobalSku getBySkuNo(@Param("skuNo") String skuNo);

}
