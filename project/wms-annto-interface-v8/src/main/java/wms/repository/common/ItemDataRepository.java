package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ItemData;

import java.util.List;

public interface ItemDataRepository extends BaseRepository<ItemData, String> {

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and i.itemNo = :itemNo or i.skuNo = :itemNo")
    List<ItemData> getByItemNo(@Param("warehouse") String warehouse,
                               @Param("itemNo") String itemNo);

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and i.itemNo = :itemNo and i.clientId=:client")
    ItemData getByItemCode(@Param("warehouse") String warehouse,@Param("client") String client,
                               @Param("itemNo") String itemNo);

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and i.clientId = :client and i.itemNo = :itemNo and i.entityLock=:entityLock")
    ItemData getByItemNo(@Param("warehouse") String warehouse,
                         @Param("client") String client,
                         @Param("itemNo") String itemNo,
                         @Param("entityLock") Integer entityLock);

    @Query("select i from ItemData i " +
            " where i.itemDataGlobalId = :itemDataGlobalId")
    List<ItemData> getByItemDataGlobal(@Param("itemDataGlobalId") String itemDataGlobalId);

    @Query("select i from ItemData i " +
            " where i.warehouseId = :warehouse and i.itemNo = :itemNo or i.skuNo = :itemNo")
    ItemData getBySkuNo(@Param("warehouse") String warehouse,
                               @Param("itemNo") String itemNo);

    @Query("select i from ItemData i " +
            " where i.itemNo = :itemNo and i.clientId=:client")
    List<ItemData> getByItemNoAndClientId(@Param("itemNo") String itemNo,@Param("client") String id);

    @Query("select i from ItemData i " +
            " where i.itemNo = :itemNo and i.clientId=:client")
    ItemData getOneByItemNoAndClientId(@Param("itemNo") String itemNo,@Param("client") String id);
}
