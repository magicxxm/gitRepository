package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface ItemDataRepository extends BaseRepository<ItemData,String> {

    @Query("select i from ItemData i where i.itemNo = :itemNo and i.clientId = :clientId and i.warehouseId = :warehouseId")
    ItemData getByItemNo(@Param("itemNo") String itemNo,
                         @Param("clientId")String clientId,
                         @Param("warehouseId")String warehouseId);

    @Query("select i from ItemData i " +
            " where i.itemNo = :itemNo and i.clientId=:client")
    ItemData getByItemNoAndClientId(@Param("itemNo") String itemNo,@Param("client") String id);

    @Query("select i from ItemData i " +
            " where i.itemNo = :itemNo")
    ItemData getByItemNo(@Param("itemNo") String itemNo);
}
