package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.PQAEnroutePod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/3/29.
 */
public interface PQAEnrouteRepositroy extends BaseRepository<PQAEnroutePod,String> {

    @Query("select p from PQAEnroutePod p where p.podId = :pod and" +
            " p.stationId = :station and p.warehouesId = :warehouseId")
    List<PQAEnroutePod> getByPodIdAndStationIdAndWarehouesId(@Param("pod")String podId,@Param("station")String stationId,
                                                             @Param("warehouseId")String warehouseId);
}
