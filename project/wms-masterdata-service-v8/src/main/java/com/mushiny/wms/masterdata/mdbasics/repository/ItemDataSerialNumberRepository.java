package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataSerialNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemDataSerialNumberRepository extends BaseRepository<ItemDataSerialNumber,String>{

    @Query("select i from ItemDataSerialNumber i where i.serialNo = :serialNo")
    ItemDataSerialNumber getBySerialNo(@Param("serialNo") String serialNo);
}
