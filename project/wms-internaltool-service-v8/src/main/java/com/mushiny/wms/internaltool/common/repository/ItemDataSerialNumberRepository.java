package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.ItemDataSerialNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemDataSerialNumberRepository extends BaseRepository<ItemDataSerialNumber, String> {


    @Query("select i from ItemDataSerialNumber i where i.serialNo = :sn")
    ItemDataSerialNumber getBySN(@Param("sn") String sn);
}
