package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.StockUnit;
import com.mushiny.wms.outboundproblem.domain.common.StockUnitRecord;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockUnitRecordRepository extends BaseRepository<StockUnitRecord, String> {

    @Query(" select s from StockUnitRecord s " +
            " where s.itemNo = :itemNo order by s.operator ")
    StockUnitRecord getByItemNo(@Param("itemNo") String itemNo);

    @Query(" select s from StockUnitRecord s " +
            " where s.itemNo = :itemNo and s.toStockUnit=:stockUnitId ")
    StockUnitRecord getByStockUnitId(@Param("stockUnitId") String stockUnitId,@Param("itemNo") String itemNo);

}
