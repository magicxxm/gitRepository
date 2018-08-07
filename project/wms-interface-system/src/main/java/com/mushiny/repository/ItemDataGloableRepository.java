package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemData;
import com.mushiny.model.ItemDataGlobal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface ItemDataGloableRepository extends BaseRepository<ItemDataGlobal,String> {

    @Query(" select i from ItemDataGlobal i where i.itemNo = :itemNo or i.skuNo = :itemNo")
    ItemDataGlobal getByItemDataNo(@Param("itemNo") String itemNo);

}
