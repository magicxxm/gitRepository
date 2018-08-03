package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.Lot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface LotRepository extends BaseRepository<Lot, String> {

    @Query("select l from Lot l where l.lotNo = :lotNo")
    Lot getByLotNo(@Param("lotNo") String lotNo);

    @Query("select l from Lot l where l.itemData = :itemData and l.useNotAfter = :useNotAfter")
    Lot getByItemData(@Param("itemData") ItemData itemData, @Param("useNotAfter") LocalDate useNotAfter);
}
