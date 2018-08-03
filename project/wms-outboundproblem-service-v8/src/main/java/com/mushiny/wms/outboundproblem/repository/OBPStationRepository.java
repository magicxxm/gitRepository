package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPStation;
import com.mushiny.wms.outboundproblem.domain.common.RcsTrip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPStationRepository extends BaseRepository<OBPStation, String> {

    @Query(" select o from OBPStation o where o.warehouseId = :warehouseId and o.name = :name")
    OBPStation getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);

    @Query(" select s from RcsTrip s where s.workStationId.id =:workStationId and s.warehouseId =:warehouseId and  s.tripType = 'OBPPod' and s.tripState <> 'Finish' and s.tripState <> 'Leaving'")
    List<RcsTrip> getRcsTrip(@Param("workStationId") String workStationId, @Param("warehouseId") String warehouseId);

    @Query(" select s from RcsTrip s where s.workStationId.id =:workStationId and s.warehouseId =:warehouseId and s.tripState <>'Finish'")
    List<RcsTrip> getRcsTripState(@Param("workStationId") String workStationId, @Param("warehouseId") String warehouseId);
}
