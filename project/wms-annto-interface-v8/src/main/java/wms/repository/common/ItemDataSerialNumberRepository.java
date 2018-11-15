package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.ItemDataSerialNumber;

import java.util.List;

public interface ItemDataSerialNumberRepository extends BaseRepository<ItemDataSerialNumber,String> {

    @Query("select i from ItemDataSerialNumber i where i.serialNo = :serialNo and i.itemDataId=:itemDataid and i.entityLock=0")
    ItemDataSerialNumber getBySerialNoAndItemData(@Param("serialNo") String serialNo, @Param("itemDataid") String itemData);

    @Query("select i from ItemDataSerialNumber i where i.serialNo = :serialNo and i.entityLock = 0")
    ItemDataSerialNumber getBySerialNo(@Param("serialNo") String serialNo);

    @Query("select i from ItemDataSerialNumber i where i.itemDataId=:itemDataId and i.clientId=:clientId")
    List<ItemDataSerialNumber> getByItemDataIdAndClientId(@Param("itemDataId")String itemId,@Param("clientId")String clientId);
}
