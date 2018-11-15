package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.ItemData;
import com.mushiny.wms.schedule.domin.StockUnit;
import com.mushiny.wms.schedule.domin.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockUnitRepository extends BaseRepository<StockUnit,String> {

    @Query("select count(su.itemData) from StockUnit su where su.unitLoad=:unitload and su.entityLock<>2 and su.amount>0")
    long countByUnitLoad(@Param("unitload") UnitLoad unitLoad);

    @Query("select su.itemData from StockUnit su where su.unitLoad=:unitload and su.itemData=:itemData and su.entityLock<>2 and su.amount>0")
    List<ItemData> itemByUnitLoad(@Param("unitload") UnitLoad unitLoad, @Param("itemData") ItemData itemData);

    //根据unitLoad获取所有StockUnit
    @Query("select su from StockUnit su where su.unitLoad=:unitLoad and su.entityLock<>2 and su.amount>0")
    List<StockUnit> getAllByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query("select su from StockUnit su where su.itemData=:itemData and su.unitLoad=:unitLoad and su.entityLock<>2")
    StockUnit getByItemDataAndUnitLoad(@Param("itemData") ItemData itemData, @Param("unitLoad") UnitLoad unitLoad);

    @Query("select s from StockUnit s where s.itemData=:itemData and s.warehouseId=:warehouseId and s.clientId=:clientId and  s.unitLoad=:unitLoad and s.entityLock = :entityLock")
    List<StockUnit> getByUnitLoadAndEntityLock(@Param("itemData") ItemData itemData,
                                               @Param("warehouseId") String warehouseId,
                                               @Param("clientId") String clientId,
                                               @Param("unitLoad") UnitLoad unitLoad,
                                               @Param("entityLock") int entityLock);

}
