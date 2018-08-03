package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;
import com.mushiny.wms.masterdata.mdbasics.domain.PodTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PodTypePositionRepository extends BaseRepository<PodTypePosition, String> {

    @Query(" select b from PodTypePosition b " +
            " where b.podType = :podType  order by b.positionNo")
    List<PodTypePosition> getByPodType(@Param("podType") PodType podType);

}
