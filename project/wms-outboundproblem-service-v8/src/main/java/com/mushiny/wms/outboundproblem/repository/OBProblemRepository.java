package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.domain.common.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OBProblemRepository extends BaseRepository<OBProblem, String> {

    @Query(" select o from OBProblem o , CustomerShipment cs " +
            " where o.warehouseId = :warehouseId and cs.shipmentNo = :shipmentNo and o.shipmentId = cs.id")
    List<OBProblem> getByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query("select coalesce(sum (i.amount),0) from OBProblemCheck i " +
            "where i.problemStoragelocation =:problemStorageLocation " +
            "and i.state = 'unsolved' " +
            "and i.problemType = 'MORE' ")
    BigDecimal sumByProblemStorageLocationAndOpen(@Param("problemStorageLocation") String problemStorageLocation);

    @Query("SELECT count(a.id) FROM OBProblem a " +
            " WHERE a.state <> 'CLOSE' " +
            " AND a.problemStoragelocation = :problemStorageLocation " +
            " AND a.id <> :id ")
    Integer countIdByContainer(@Param("problemStorageLocation") String problemStorageLocation,
                               @Param("id") String id);

    @Query(" SELECT s FROM StorageLocation s where s.name = :problemStorageLocation ")
    StorageLocation getByStorageLocationName(@Param("problemStorageLocation") String problemStorageLocation);

    @Query(" SELECT coalesce(sum (s.amount),0) from StockUnit as s " +
            "where s.unitLoad.id =:unitLoadId")
    BigDecimal sumByUnitLoadId(@Param("unitLoadId") String unitLoadId);

    @Query(" SELECT u from UnitLoad as u where  u.entityLock = 1  and  u.storageLocation.id=:storageLocationId ")
    UnitLoad getUnitLoadByStorageLocationId(@Param("storageLocationId") String storageLocationId);

    @Query("select r from ReceiveProcess as r where  r.entityLock = 0  and  r.storageLocation.id = :storageLocationId ")
    ReceiveProcess getReceiveProcessByStorageLocationId(@Param("storageLocationId") String storageLocationId);


    //获取容器商品
    @Query(" select sum(s.amount) as amount, " +
            " s.unitLoad.storageLocation.name as storageLocationId, " +
            " s.lot.lotNo as lotNo, " +
            " s.itemData.id as itemDataId, " +
            " s.itemData.itemNo as itemNo," +
            " s.warehouseId as warehouseId " +
            "  FROM StockUnit s where s.amount<>0 " +
            "and s.unitLoad.storageLocation.id =:storageLocationId " +
            "group by s.unitLoad.storageLocation.name,s.lot.lotNo,s.itemData ,s.itemData.itemNo,s.warehouseId ")
    List<Object[]> getStockUnitByStorageLocationId(@Param("storageLocationId") String storageLocationId);

    @Query("select i from ItemData i where i.itemNo = :itemNo ")
    ItemData getByItemData(@Param("itemNo") String itemNo);

    @Query(nativeQuery = true, value = "SELECT u.name FROM User u where u.id =:userId ")
    String getByUserId(@Param("userId") String userId);

    @Query("SELECT u.name FROM User u where u.id =:userId ")
    String getByUserIds(@Param("userId") String userId);

    @Query("select i from ItemDataGlobal i where i.itemNo =:itemNo or i.skuNo =:skuNo ")
    List<ItemDataGlobal> getByItemNoOrSkuNo(@Param("itemNo") String itemNo, @Param("skuNo") String skuNo);

    @Query("select i.itemNo from ItemDataGlobal i where i.skuNo =:skuNo ")
    String getBySkuNo(@Param("skuNo") String skuNo);

    @Query("select s from StorageLocation s where s.name =:storageLocationName ")
    StorageLocation getByProblemStorageLocationName(@Param("storageLocationName") String storageLocationName);

    @Query(" select o from OBProblem o where o.container = :container")
    List<OBProblem> getByContainer(@Param("container") String container);

    @Query(" select o from OBProblem o where o.problemStoragelocation = :storagelocation and o.itemNo = :itemdataSku and o.state <> 'CLOSE' ")
    OBProblem getByStorageLocationNameAndSku(@Param("storagelocation") String storagelocation, @Param("itemdataSku") String itemdataSku);

//    @Query(" select o from OBProblem o where o.container = :container and o.itemNo = :itemNo and o.state <> 'CLOSE'")
//    OBProblem getByStorageLocationNameAndSku(@Param("container") String storagelocation, @Param("itemNo") String itemDataId);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId")
    List<OBProblem> getByShipmentAndItem(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);

    @Query("SELECT o FROM OBProblem o WHERE o.problemType = 'UNABLE_SCAN_SN' AND o.shipmentId = :shipmentId AND (o.itemNo = :itemNo OR o.skuNo=:itemNo) AND o.warehouseId = :warehouseId ")
    OBProblem getUnableScanSn(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemNo") String itemNo);

    @Query("SELECT o FROM OBProblem o WHERE o.problemType = 'DAMAGED' AND o.shipmentId = :shipmentId AND (o.itemNo = :itemNo OR o.skuNo=:itemNo) AND o.warehouseId = :warehouseId ")
    OBProblem getDamagedByItem(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemNo") String itemNo);

    @Query("SELECT o FROM OBProblem o WHERE o.problemType = 'UNABLE_SCAN_SKU' AND o.shipmentId = :shipmentId AND o.itemDataId=:itemDataId and o.warehouseId = :warehouseId ")
    OBProblem getScanskuByItem(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);

    @Query(" select o from OBProblem o , CustomerShipment cs " +
            " where o.warehouseId = :warehouseId and cs.shipmentNo = :shipmentNo and o.shipmentId = cs.id")
    List<OBProblem> getByShipment(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId and o.problemType='LOSE'")
    OBProblem getByShipmentAndItemLose(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId and o.problemType='UNABLE_SCAN_SN'")
    OBProblem getByShipmentAndItemSN(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);

    @Query(" select o from OBProblem o , CustomerShipment cs " +
            " where o.warehouseId = :warehouseId and cs.shipmentNo = :shipmentNo and o.shipmentId = cs.id")
    List<OBProblem> getProblemByShipmentNo(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo);

    @Query(" select o from OBProblem o , CustomerShipment cs where o.warehouseId = :warehouseId " +
            "and o.shipmentId = cs.id and cs.shipmentNo=:shipmentNo and o.itemDataId = :itemDataId and o.problemType='LOSE'")
    OBProblem getByShipmentItemLose(@Param("warehouseId") String warehouseId, @Param("shipmentNo") String shipmentNo, @Param("itemDataId") String itemDataId);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId and o.problemType<>'UNABLE_SCAN_SKU'")
    List<OBProblem> getByShipmentAndItemId(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId and o.problemType=:problemType")
    List<OBProblem> getByShipmentAndType(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId,@Param("problemType") String problemType);

    @Query(" select o from OBProblem o where o.warehouseId = :warehouseId and o.shipmentId = :shipmentId and o.itemDataId = :itemDataId and (o.problemType='DAMAGED' or o.problemType='UNABLE_SCAN_SN')")
    List<OBProblem> getByShipmentIdAndType(@Param("warehouseId") String warehouseId, @Param("shipmentId") String shipmentId, @Param("itemDataId") String itemDataId);
}

