package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AdviceRequestPositionRepository extends BaseRepository<AdviceRequestPosition, String> {

    @Query(" select coalesce(sum (ap.notifiedAmount),0) from AdviceRequestPosition ap " +
            " where ap.adviceRequest = :adviceRequest and ap.itemData = :itemData")
    BigDecimal sumByItemData(@Param("adviceRequest") AdviceRequest adviceRequest,
                             @Param("itemData") ItemData itemData);

    @Query("select ap from AdviceRequestPosition ap " +
            " where ap.adviceRequest = :adviceRequest and ap.itemData = :itemData")
    List<AdviceRequestPosition> getByAdviceRequestAndItemData(@Param("adviceRequest") AdviceRequest adviceRequest,
                                                              @Param("itemData") ItemData itemData);
    @Query("select ap from AdviceRequestPosition ap " +
            " where ap.adviceRequest.id = :adviceRequest")
    List<AdviceRequestPosition> getByAdviceRequestId(@Param("adviceRequest") String adviceRequest);

    @Query("select ap from AdviceRequestPosition ap " +
            " where ap.warehouseId = :warehouse and ap.itemData.itemNo = :sku")
    List<AdviceRequestPosition> getBySku(@Param("warehouse") Warehouse warehouse,
                                         @Param("sku") String sku);

//    @Query("select ap from AdviceRequestPosition ap " +
//            " where ap.warehouseId = :warehouse and ap.clientId = :client and ap.itemData.itemNo = :sku")
//    List<AdviceRequestPosition> getBySku(@Param("warehouse") Warehouse warehouse,
//                                         @Param("client") Client client,
//                                         @Param("sku") String sku);
}
