package com.mushiny.wms.outboundproblem.repository.common;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface LocationRepository extends BaseRepository<OBPLocation,String> {
    @Query("select o from OBPLocation o where o.solveId=:solveId and o.warehouseId=:warehouseId")
    List<OBPLocation> getBySolveId (@Param("warehouseId") String warehouseId, @Param("solveId") String solveId);

    @Query("select o from OBPLocation o where o.cellName=:cellName and o.warehouseId=:warehouseId and o.state<>'solved'")
    List<OBPLocation> getByCellName (@Param("warehouseId") String warehouseId, @Param("cellName") String cellName);

    @Query("select o from OBPLocation o where o.location=:location and o.warehouseId=:warehouseId and o.isCallPod=false")
    List<OBPLocation> getByLocation(@Param("warehouseId") String warehouseId, @Param("location") String location);

    @Query("select o from OBPLocation o where o.location like CONCAT('%',:podNo,'%') and o.warehouseId=:warehouseId and o.isCallPod=false")
    List<OBPLocation> getByPodNo(@Param("warehouseId") String warehouseId, @Param("podNo") String podNo);

    @Query("select o from OBPLocation o where o.location=:location and o.warehouseId=:warehouseId and o.itemNo=:itemNo and o.isCallPod=false")
    List<OBPLocation> getByLocationAndItemNo(@Param("warehouseId") String warehouseId, @Param("location") String location,@Param("itemNo") String itemNo) ;

    @Query("select o from OBPLocation o where o.location=:location and o.warehouseId=:warehouseId and  o.cellName=:cellName and o.amountScaned<o.amount and o.isCallPod=false")
    List<OBPLocation> getByCellAndLocation(@Param("warehouseId") String warehouseId, @Param("location") String location, @Param("cellName") String cellName);
}
