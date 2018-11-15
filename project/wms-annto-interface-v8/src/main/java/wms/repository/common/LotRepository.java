package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import wms.common.respository.BaseRepository;
import wms.domain.ItemData;
import wms.domain.common.Lot;

import java.util.Date;

/**
 * Created by 123 on 2017/12/14.
 */
public interface LotRepository extends BaseRepository<Lot,String> {

    @Query("select l from Lot l where l.lotNo = :lotNo")
    Lot getByLotNo(@Param("lotNo")String lotNo);

    @Query("select l from Lot l where l.lotDate = :lotDate and l.itemData=:itemData")
    Lot getByLotDate(@Param("lotDate")Date lotDate, @Param("itemData")ItemData itemData);
}
