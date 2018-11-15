package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.ItemDataSerialNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataSerialNumberRepository extends BaseRepository<ItemDataSerialNumber,String> {

    @Query("select i from ItemDataSerialNumber i where i.itemData.id=:itemDataId and i.serialNo = :serialNo")
    ItemDataSerialNumber getBySerialNo(@Param("itemDataId") String itemDataId, @Param("serialNo") String serialNo);

    @Query("select i from ItemDataSerialNumber i where i.itemData.id=:itemDataId and i.entityLock =0")
    List<ItemDataSerialNumber> getByEntityLock(@Param("itemDataId") String itemDataId);

}
