package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.StockUnitRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StockUnitRecordRepository extends BaseRepository<StockUnitRecord, String> {

    @Query("select coalesce(sum(s.amount),0) from StockUnitRecord s where s.modifiedDate>:startDate" )
     BigDecimal getByDate(@Param("startDate") LocalDateTime startDate);

}
