package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockUnitRepository extends BaseRepository<StockUnit, String> {

//    @Query(" select count (distinct s.itemData) from StockUnit s " +
//            " where s.storageLocation = :storageLocation")
//    long countByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);
//
//    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
//            " where s.container = :container ")
//    BigDecimal sumByContainer(@Param("container") Container container);
//
//    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
//            " where s.storageLocation = :storageLocation ")
//    BigDecimal sumByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);
//
//    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
//            " where s.storageLocation = :storageLocation and s.itemData = :itemData ")
//    BigDecimal sumByStorageLocationAndItemData(@Param("storageLocation") StorageLocation storageLocation,
//                                               @Param("itemData") ItemData itemData);

//    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
//            " where s.itemData = :itemData and s.storageLocation != null")
//    BigDecimal sumByItemData(@Param("itemData") ItemData itemData);
//
//    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
//            " where s.container = :container and s.itemData = :itemData")
//    BigDecimal sumByContainerAndItemData(@Param("container") Container container,
//                                         @Param("itemData") ItemData itemData);
//
//    @Query(" select s from StockUnit s where s.container = :container")
//    List<StockUnit> getByContainer(@Param("container") Container container);
//
//    @Query(" select s from StockUnit s " +
//            " where s.container = :container and s.itemData = :itemData")
//    StockUnit getByContainerAndItemData(@Param("container") Container container,
//                                              @Param("itemData") ItemData itemData);
//
//    @Query(" select s from StockUnit s " +
//            " where s.container = :container and s.itemData.itemNo = :itemNo ")
//    StockUnit getByContainerAndItemNo(@Param("container") Container container,
//                                      @Param("itemNo") String itemNo);

//    @Query(" select s from StockUnit s " +
//            " where s.container.containerType.name like '%Cubi_Scan%' and s.itemData.itemNo = :itemNo ")
//    List<StockUnit> getCubiScanItemNoByItemNo(@Param("itemNo") String itemNo);
//
//    @Query(" select s from StockUnit s " +
//            " where s.storageLocation = :storageLocation ")
//    List<StockUnit> getByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);
//
//    @Query(" select s from StockUnit s " +
//            " where s.storageLocation = :storageLocation and s.itemData = :itemData ")
//    StockUnit getByStorageLocationAndItemData(@Param("storageLocation") StorageLocation storageLocation,
//                                              @Param("itemData") ItemData itemData);
//
//    @Query(" select s from StockUnit s " +
//            " where s.storageLocation = :storageLocation and s.itemData.itemNo = :itemNo ")
//    StockUnit getByStorageLocationAndItemNo(@Param("storageLocation") StorageLocation storageLocation,
//                                            @Param("itemNo") String itemNo);
//
//    @Query(" select s from StockUnit s " +
//            " where s.storageLocation = :storageLocation " +
//            " and s.itemData.itemNo = :itemNo " +
//            " and s.itemData.client <> :client")
//    StockUnit getByItemNoAndUnequalClient(@Param("storageLocation") StorageLocation storageLocation,
//                                          @Param("client") Client client,
//                                          @Param("itemNo") String itemNo);
//
//
//    @Query(" select s from StockUnit s " +
//            " where s.itemData.itemNo = :itemNo " +
//            " and ( s.storageLocation.name = :source or s.container.name = :source)")
//    StockUnit getBySourceAndItemNo(@Param("source") String source,
//                                    @Param("itemNo") String sku);

//    @Query(" select s from StockUnit s " +
//            " where s.warehouse = :warehouse and s.itemData.itemNo = :itemNo")
//    List<StockUnit> getByItemNo(@Param("warehouse") Warehouse warehouse,
//                                @Param("itemNo") String itemNo);
//
//    @Query(" select s from StockUnit s " +
//            " where s.warehouse = :warehouse and s.client = :client and s.itemData.itemNo = :itemNo")
//    List<StockUnit> getByItemNo(@Param("warehouse") Warehouse warehouse,
//                                @Param("client") Client client,
//                                @Param("itemNo") String itemNo);
    @Query(" select s from StockUnit s " +
            " where s.unitLoad.id = :id ")
    List<StockUnit> getByUnitLoadId(@Param("id") String id);
}
