package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiveCategoryRepository extends BaseRepository<ReceiveCategory, String> {

    @Query("select r from ReceiveCategory r " +
            " where r.warehouseId = :warehouse and r.clientId = :client and r.name = :name")
    ReceiveCategory getByName(@Param("warehouse") String warehouse,
                              @Param("client") String client,
                              @Param("name") String name);

    @Query("select r from ReceiveCategory r " +
            " where r.warehouseId = :warehouse and r.clientId = :client and r.categoryType = :categoryType " +
            " order by r.orderIndex")
    List<ReceiveCategory> getByReceivingType(@Param("warehouse") String warehouse,
                                             @Param("client") String client,
                                             @Param("categoryType") String categoryType);

}
