package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/3/29.
 */
public interface IBEnrouteRepository extends BaseRepository<IBEnroutePod,String> {

    @Query("select i from IBEnroutePod i where i.podId = :pod and i.stowStationId = :station and i.warehouseId = :warehouseId")
    List<IBEnroutePod> getByPodIdAndStowStationIdAndWarehouseId(@Param("pod")String podId,
                                                                @Param("station")String station,
                                                                @Param("warehouseId")String warehouse);
}
