package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdviceRequestRepository extends BaseRepository<AdviceRequest, String> {

    @Query("select a from AdviceRequest a where a.adviceNo = :adviceNo")
    AdviceRequest getByAdviceNo(@Param("adviceNo") String adviceNo);

}
