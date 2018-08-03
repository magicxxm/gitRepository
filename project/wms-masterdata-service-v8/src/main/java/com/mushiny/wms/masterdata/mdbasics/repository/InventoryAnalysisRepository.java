package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.InventoryAnalysis;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryAnalysisRepository extends BaseRepository<InventoryAnalysis, String> {

    @Query(" select a from InventoryAnalysis a " +
            " where a.warehouseId = :warehouse and a.clientId = :client and a.itemData = :itemData")
    InventoryAnalysis getByItem(@Param("warehouse") String warehouse,
                                @Param("client") String client,
                                @Param("itemData") ItemData itemData);

    @Query(" select a from InventoryAnalysis a  where  a.level = :level " +
            "and a.warehouseId=:warehouse " +
            "and a.clientId=:client")
    List<InventoryAnalysis> getBySabcRule(@Param("warehouse") String warehouse,
                                          @Param("client") String client,
                                          @Param("level") String level);

}
