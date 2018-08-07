package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemData;
import com.mushiny.model.Lot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * Created by 123 on 2018/2/8.
 */
public interface LotRepository extends BaseRepository<Lot,String> {

    @Query("select l from Lot l where l.bestBeforeEnd = :bestBeforeEnd and l.itemData = :itemData and l.clientId = :clientId")
    Lot getByDate(@Param("bestBeforeEnd") LocalDate bestBeforeEnd,
                  @Param("itemData") ItemData itemData,@Param("clientId")String id);

    @Query("select l from Lot l where l.number = :lotNo")
    Lot getByLotNo(@Param("lotNo") String lotNo);
}
