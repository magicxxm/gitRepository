package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBPStation;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPSolveRepository extends BaseRepository<OBPSolve, String> {

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo = :shipmentNo " +
            "and o.obpStation.id = :obpStationId and o.obpWall.id = :obpWallId and o.state = :state")
    List<OBPSolve> getByShipmentNo(@Param("warehouseId") String warehouseId, @Param("obpStationId") String obpStationId,
                                   @Param("obpWallId") String obpWallId, @Param("shipmentNo") String shipmentNo,
                                   @Param("state") String state);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo = :shipmentNo ")
    List<OBPSolve> getByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo = :shipmentNo " +
            "and o.itemData.itemNo=:itemNo and o.obproblem.id is not null ")
    List<OBPSolve> getProblemGoodsByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo,@Param("itemNo") String itemNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.itemData.itemNo = :itemDataNo and o.obpWall.id = :obpWallId " +
            "and o.obproblem.id is not null and o.obpCell is not null and o.additionalContent='HAS_HOT_PICK' and o.state <> 'solved'")
    List<OBPSolve> getCellByItemData(@Param("warehouseId") String warehouseId, @Param("itemDataNo") String itemDataNo, @Param("obpWallId") String obpWallId);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo = :shipmentNo " +
            "and o.obproblem.id is null")
    List<OBPSolve> getAllGoodsByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId " +
            "and o.customerShipment.shipmentNo = :shipmentNo and o.itemData.itemNo = :itemNo and o.obproblem.id is null")
    OBPSolve getGoodsByShipmentAndItem(@Param("warehouseId") String warehouseId,
                                           @Param("shipmentNo") String shipmentNo, @Param("itemNo") String itemNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId " +
            "and o.customerShipment.shipmentNo = :shipmentNo and o.itemData.itemNo = :itemNo and o.obproblem.id is not null")
    List<OBPSolve> getProblemGoodsByShipmentAndItem(@Param("warehouseId") String warehouseId,
                                       @Param("shipmentNo") String shipmentNo, @Param("itemNo") String itemNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId " +
            "and o.customerShipment.shipmentNo = :shipmentNo and o.obproblem.id is null")
    List<OBPSolve> getGoodsByShipment(@Param("warehouseId") String warehouseId,
                                       @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId " +
            "and o.customerShipment.shipmentNo = :shipmentNo and o.obproblem.id is not null")
    List<OBPSolve> getProblemGoodsByShipment(@Param("warehouseId") String warehouseId,
                                      @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpCell.name = :cellName and o.obpWall.id = :obpWallId and o.state <> 'solved' ")
    List<OBPSolve> getByCell(@Param("warehouseId") String warehouseId, @Param("obpWallId") String obpWallId, @Param("cellName") String cellName);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpCell.name = :cellName and o.itemData.itemNo = :itemNo and o.obpWall.id = :obpWallId " +
            "and o.obproblem.id is not null and o.obproblem.amount>0 and o.obpCell is not null and o.additionalContent='HAS_HOT_PICK'")
    List<OBPSolve> getByCellAndItem(@Param("warehouseId") String warehouseId, @Param("cellName") String cellName, @Param("itemNo") String itemNo, @Param("obpWallId") String obpWallId);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpCell.name = :cellName and o.itemData.itemNo = :itemNo and o.obpWall.id = :obpWallId " +
            "and o.obproblem.id is not null and o.obproblem.amount>0 and o.obpCell is not null and o.additionalContent='HAS_HOT_PICK'and o.amountScanedProblem<o.amountProblem" )
    List<OBPSolve> getByCellAndItemAmount(@Param("warehouseId") String warehouseId, @Param("cellName") String cellName, @Param("itemNo") String itemNo, @Param("obpWallId") String obpWallId);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpCell.name = :cellName and o.obpWall.id = :obpWallId and o.obproblem.id is not null " +
            "and o.obproblem.amount>0 and o.obpCell is not null and o.additionalContent='HAS_HOT_PICK'")
    List<OBPSolve> getProblemByCell(@Param("warehouseId") String warehouseId, @Param("cellName") String cellName, @Param("obpWallId") String obpWallId);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpCell.name = :cellName and o.customerShipment.shipmentNo = :shipmentNo " +
            "and o.obpStation.id = :obpStationId ")
    List<OBPSolve> getByCellAndShipmentAndStation(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo,
                                                  @Param("obpStationId") String obpStationId, @Param("cellName") String cellName);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obproblem.id = :problemId and o.customerShipment.shipmentNo = :shipmentNo " +
            "and o.obpStation.id = :obpStationId ")
    OBPSolve getByProblemAndShipmentAndStation(@Param("warehouseId") String warehouseId, @Param("problemId") String problemId,
                                                  @Param("shipmentNo") String shipmentNo, @Param("obpStationId") String obpStationId);

    @Query(" select o from OBPSolve o where o.warehouseId = :warehouseId and o.obpStation.id = :obpStationId " +
            "and o.obpWall.id = :obpWallId and o.state = :state and o.obproblem.id is null and o.obpCell is not null " )
    List<OBPSolve> getByStationAndWallAndHaveCell(@Param("warehouseId") String warehouseId, @Param("obpStationId") String obpStationId,
                                            @Param("obpWallId") String obpWallId, @Param("state") String state);

    @Query("select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo=:shipmentNo " +
            "and o.itemData.itemNo=:itemNo and o.obproblem.problemType='UNABLE_SCAN_SKU'")
    OBPSolve getByAmountShipmentAndSku(@Param("warehouseId") String warehouseId,@Param("shipmentNo") String shipmentNo,@Param("itemNo") String itemNo);

    @Query("select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo=:shipmentNo " +
            "and o.itemData.itemNo=:itemNo and o.obproblem is not null")
    List<OBPSolve> getByAmountShipmentAndProblem(@Param("warehouseId") String warehouseId,@Param("shipmentNo") String shipmentNo,@Param("itemNo") String itemNo);

    @Query("select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo=:shipmentNo " +
            "and o.itemData.itemNo=:itemNo and o.obproblem.problemType='UNABLE_SCAN_SN'")
    OBPSolve getByAmountShipmentAndSN(@Param("warehouseId") String warehouseId,@Param("shipmentNo") String shipmentNo,@Param("itemNo") String itemNo);

    @Query("select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo=:shipmentNo and o.itemData.itemNo=:itemNo and o.obproblem is null")
    OBPSolve getByItemNoShipmentNo (@Param("warehouseId") String warehouseId,@Param("itemNo") String itemNo,@Param("shipmentNo") String shipmentNo);

    @Query("select o from OBPSolve o where o.warehouseId = :warehouseId and o.customerShipment.shipmentNo=:shipmentNo " +
            "and o.obproblem is not null and o.obproblem.amount>0 and o.obproblem.problemType<>'UNABLE_SCAN_SKU'")
    List<OBPSolve> getByShipmentAndProblemType(@Param("warehouseId") String warehouseId,@Param("shipmentNo") String shipmentNo);

    @Query(" select s from OBPSolve s where s.obproblem.id=:problemId")
    OBPSolve getByProblemId(@Param("problemId") String problemId);

}
