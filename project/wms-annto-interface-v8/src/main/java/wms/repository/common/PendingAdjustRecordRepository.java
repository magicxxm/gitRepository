package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.PendingAdjustRecord;

import java.util.List;
/**
 * Created by ljp on 2018/01/04.
 */
public interface PendingAdjustRecordRepository extends BaseRepository<PendingAdjustRecord, String> {

    @Query(" select sur from PendingAdjustRecord  sur where sur.id = :id ")
    PendingAdjustRecord getPendingAdjust(@Param("id") String id);

}
