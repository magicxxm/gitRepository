package com.mushiny.wms.outboundproblem.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPSolvePositionRepository extends BaseRepository<OBPSolvePosition, String> {

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.shipmentNo = :shipmentNo and o.solveKey = :solveKey")
    OBPSolvePosition getForceDeleteSolvePositionByShipmentNoAndSolveKey(@Param("warehouseId") String warehouseId,
                                                                        @Param("shipmentNo") String shipmentNo,
                                                                        @Param("solveKey") String solveKey);

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.obpSolve.id = :solveId " +
            "and o.shipmentNo = :shipmentNo")
    OBPSolvePosition getByShipmentNo(@Param("warehouseId") String warehouseId,
                                     @Param("solveId") String solveId,
                                     @Param("shipmentNo") String shipmentNo );

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.obpSolve.id = :solveId " +
            "and o.itemDataNo = :itemNo and o.shipmentNo = :shipmentNo ")
    OBPSolvePosition getByShipmentNoAndItemNo(@Param("warehouseId") String warehouseId,
                                             @Param("solveId") String solveId,
                                             @Param("shipmentNo") String shipmentNo,
                                             @Param("itemNo") String itemNo);

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.shipmentNo = :shipmentNo and o.obpSolve.id = :solveId")
    OBPSolvePosition getSolvePositionByShipmentNoAndSolve(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo,
                                                          @Param("solveId") String solveId);

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.solveKey ='DISMANTLE_SHIPMENT' and o.shipmentNo =:shipmentNo")
    List<OBPSolvePosition> getBySolveKeyAndShipmentNo(@Param("warehouseId") String warehouseId,@Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBPSolvePosition o where o.warehouseId = :warehouseId and o.solveKey = :solveKey and o.obpSolve.id = :solveId")
    OBPSolvePosition getBySolveIdAndSolveKey(@Param("warehouseId") String warehouseId, @Param("solveKey") String solveKey,
                                                @Param("solveId") String solveId);
}
