package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataSerialNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataSerialNumberRepository extends BaseRepository<ItemDataSerialNumber,String> {

    @Query("select i from ItemDataSerialNumber i where i.serialNo = :serialNo and i.entityLock = 0")
    ItemDataSerialNumber getBySerialNo(@Param("serialNo") String serialNo);

    @Query("select i from ItemDataSerialNumber i where i.itemData.id = :itemDataId and i.entityLock = 0")
    List<ItemDataSerialNumber> getByItemData(@Param("itemDataId") String itemDataId);

    @Query("select i from ItemDataSerialNumber i where i.serialNo = :sn")
    ItemDataSerialNumber getBySN(@Param("sn") String sn);
}
