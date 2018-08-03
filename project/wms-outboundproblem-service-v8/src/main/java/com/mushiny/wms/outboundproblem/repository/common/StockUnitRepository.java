package com.mushiny.wms.outboundproblem.repository.common;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.StockUnit;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.common.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.math.BigDecimal;
import java.util.List;

public interface StockUnitRepository extends BaseRepository<StockUnit, String> {

    @Query(" select count (distinct s.itemData) from StockUnit s " +
            " where s.unitLoad = :unitLoad")
    long countByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select count (distinct s.itemData) from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation")
    long countByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query(" select coalesce(sum (s.amount) - sum (s.reservedAmount),0) from StockUnit s " +
            " where s.itemData = :itemData and s.unitLoad.storageLocation.storageLocationType.storageType='BIN' ")
    BigDecimal sumByItemData(@Param("itemData") ItemData itemData);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation ")
    BigDecimal sumByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.itemData = :itemData and s.unitLoad.storageLocation is not null ")
    BigDecimal sumStorageLocationItemData(@Param("itemData") ItemData itemData);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation and s.itemData = :itemData")
    BigDecimal sumByStorageLocationAndItemData(@Param("storageLocation") StorageLocation storageLocation,
                                               @Param("itemData") ItemData itemData);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData.itemNo = :itemNo")
    BigDecimal sumByUnitLoadAndItemNo(@Param("unitLoad") UnitLoad unitLoad,
                                      @Param("itemNo") String itemNo);

    @Query(" select s from StockUnit s where s.unitLoad.storageLocation = :storageLocation")
    List<StockUnit> getByStorageLocation(@Param("storageLocation") StorageLocation storageLocation);

    @Query(" select s from StockUnit s where s.unitLoad.storageLocation = :storageLocation and s.amount>0")
    List<StockUnit> getByStorageLocationAndAmount(@Param("storageLocation") StorageLocation storageLocation);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation and s.itemData = :itemData and s.amount>0")
    List<StockUnit> getByStorageLocationAndItemData(@Param("storageLocation") StorageLocation storageLocation,
                                                    @Param("itemData") ItemData itemData);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation " +
            " and s.itemData.itemNo = :itemNo " +
            " and s.itemData.clientId <> :clientId")
    StockUnit getByItemNoAndUnequalClient(@Param("storageLocation") StorageLocation storageLocation,
                                          @Param("clientId") String clientId,
                                          @Param("itemNo") String itemNo);

    @Query(" select s from StockUnit s where s.unitLoad = :unitLoad and s.unitLoad.entityLock < 2 and s.amount>0")
    List<StockUnit> getByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData = :itemData and s.unitLoad.entityLock <2  and s.amount>0")
    List<StockUnit> getByUnitLoadAndItemData(@Param("unitLoad") UnitLoad unitLoad,
                                             @Param("itemData") ItemData itemData);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData = :itemData ")
    BigDecimal sumByUnitLoadAndItemData(@Param("unitLoad") UnitLoad unitLoad,
                                        @Param("itemData") ItemData itemData);

    @Query(" select s from StockUnit s " +
            " left join s.itemData i " +
            " where s.unitLoad = :unitLoad " +
            " and i.itemNo = :itemNo " +
            " and i.clientId <> :clientId")
    StockUnit getByUnequalClientAndItemNo(@Param("unitLoad") UnitLoad unitLoad,
                                          @Param("itemNo") String itemNo,
                                          @Param("clientId") String clientId);


    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad = :unitLoad ")
    BigDecimal sumByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad.storageLocation.name = :name and s.itemData.itemNo = :itemNo and s.amount>0")
    List<StockUnit> getByNameAndItem(@Param("name") String name,
                                     @Param("itemNo") String itemNo);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad.storageLocation = :storageLocation and s.itemData.itemNo = :itemNo and s.amount>0")
    List<StockUnit> getByItemDataNo(@Param("storageLocation") StorageLocation storageLocation,
                                    @Param("itemNo") String itemNo);

    @Query(" select s from StockUnit s " +
            " where s.serialNo=:serialNo and s.unitLoad.storageLocation.name = :location and s.itemData.itemNo = :itemNo and s.unitLoad.entityLock <2  and s.amount>0")
    StockUnit getByItemNoAndSn(@Param("serialNo") String serialNo, @Param("location") String location,
                                     @Param("itemNo") String itemNo);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData = :itemData and s.serialNo=:serialNo and s.unitLoad.entityLock <2  and s.amount>0")
    StockUnit getByItemDataSn(@Param("unitLoad") UnitLoad unitLoad, @Param("itemData") ItemData itemData, @Param("serialNo") String serialNo);
}

