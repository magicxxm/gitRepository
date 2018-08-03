package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoodsReceiptRepository extends BaseRepository<GoodsReceipt, String> {

    @Query("select g from GoodsReceipt g where g.grNo = :grNo")
    GoodsReceipt getByGrNo(@Param("grNo") String grNo);

    @Query("select g from GoodsReceipt g where g.relatedAdvice = :adviceRequest")
    GoodsReceipt getByAdviceRequest(@Param("adviceRequest") AdviceRequest adviceRequest);

}
