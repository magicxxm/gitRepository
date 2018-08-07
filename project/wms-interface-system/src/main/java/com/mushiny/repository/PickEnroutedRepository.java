package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.PickEnroutePod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/3/29.
 */
public interface PickEnroutedRepository extends BaseRepository<PickEnroutePod,String> {

    @Query("select p from PickEnroutePod p where p.podId = :pod and p.stationId = :stationId and p.warehouseId = :warehouseId")
    List<PickEnroutePod> getByPodIdAndStationIdAndWarehouseId(@Param("pod")String podId,
                                                              @Param("stationId")String stationId,
                                                              @Param("warehouseId")String warehouseId);
}
