package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.AdviceRequest;

public interface AdviceRequestRepository extends BaseRepository<AdviceRequest, String> {

    @Query("select a from AdviceRequest a where a.adviceNo = :adviceNo")
    AdviceRequest getByAdviceNo(@Param("adviceNo") String adviceNo);

}
