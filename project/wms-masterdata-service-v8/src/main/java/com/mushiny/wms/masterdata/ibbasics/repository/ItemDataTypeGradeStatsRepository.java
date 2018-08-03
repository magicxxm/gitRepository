package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDataTypeGradeStatsRepository extends BaseRepository<ItemDataTypeGradeStats, String> {

    @Query("select i from ItemDataTypeGradeStats i where i.itemData = :itemData")
    ItemDataTypeGradeStats getByItemData(@Param("itemData") ItemData itemData);

    @Query("select i from ItemDataTypeGradeStats i where i.warehouseId=:warehouse " +
            "and i.clientId=:client and i.itemData = :itemData")
    ItemDataTypeGradeStats getByItemDataAnd(@Param("warehouse") String warehouse,
                                            @Param("client") String client,
                                            @Param("itemData") ItemData itemData);

    @Query("select i from ItemDataTypeGradeStats i where " +
            "i.warehouseId=:warehouse and i.clientId=:client")
    List<ItemDataTypeGradeStats> getByWarehouseIdAndClient(@Param("warehouse") String warehouse,
                                                  @Param("client") String client);

    @Query("select i from ItemDataTypeGradeStats i where i.warehouseId=:warehouse " +
            "and i.clientId=:client and i.itemData.id = :itemData")
    ItemDataTypeGradeStats getByItemDataAndWarehouse(@Param("warehouse") String warehouse,
                                                     @Param("client") String client,
                                                     @Param("itemData") String itemData);
}
