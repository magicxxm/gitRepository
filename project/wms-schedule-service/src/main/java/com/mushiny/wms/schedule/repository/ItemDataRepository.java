package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataRepository extends BaseRepository<ItemData, String> {

    @Query("select i from ItemData i " +
            " where i.clientId = :clientId")
    List<ItemData> getItemDataByClient(@Param("clientId") String clientId);

    @Query("select i from ItemData i " +
            " where i.serialRecordType<>'ALWAYS_RECORD'")
    List<ItemData> getAll();

}
