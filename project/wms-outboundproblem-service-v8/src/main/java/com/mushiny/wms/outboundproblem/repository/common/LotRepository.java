package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.Lot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LotRepository extends BaseRepository<Lot, String> {

    @Query("select l from Lot l where l.lotNo = :lotNo")
    Lot getByLotNo(@Param("lotNo") String lotNo);

    @Query("select l from Lot l where l.itemData = :itemData and l.useNotAfter = :useNotAfter")
    Lot getByItemData(@Param("itemData") ItemData itemData, @Param("useNotAfter") LocalDate useNotAfter);

    @Query("select l from Lot l where l.itemData = :itemData and l.warehouseId = :warehouseId and l.clientId = :clientId")
    List<Lot> getByItemData(@Param("itemData") ItemData itemData, @Param("itemData") String warehouseId, @Param("itemData") String clientId);
}
