package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.GoodsReceipt;
import wms.domain.common.AdviceRequest;

public interface GoodsReceiptRepository extends BaseRepository<GoodsReceipt, String> {

    @Query("select g from GoodsReceipt g where g.grNo = :grNo")
    GoodsReceipt getByGrNo(@Param("grNo") String grNo);

    @Query("select g from GoodsReceipt g where g.relatedAdvice = :adviceRequest")
    GoodsReceipt getByAdviceRequest(@Param("adviceRequest") AdviceRequest adviceRequest);

}
