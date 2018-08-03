package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.CommonUtil;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.ItemDataGlobalRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.SemblenceRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class CheckingBusiness {

    @PersistenceContext
    private EntityManager entityManager;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final StockUnitRepository stockUnitRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataRepository itemDataRepository;
    private final SemblenceRepository semblenceRepository;
    private static final Logger log = LoggerFactory.getLogger(CheckingBusiness.class);

    @Autowired
    public CheckingBusiness(ItemDataGlobalRepository itemDataGlobalRepository,
                            ApplicationContext applicationContext,
                            ItemDataRepository itemDataRepository,
                            StockUnitRepository stockUnitRepository,
                            SemblenceRepository semblenceRepository) {
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.itemDataRepository = itemDataRepository;
        this.applicationContext = applicationContext;
        this.semblenceRepository = semblenceRepository;
    }

    public boolean isBin(StorageLocation storageLocation) {
        return storageLocation.getStorageLocationType().getStorageType().equalsIgnoreCase("BIN");
    }

    public void checkingSKU(String sku) {
       /* List<ItemDataGlobal> itemDataGlobal = itemDataGlobalRepository.getBySKU(sku);
        if (itemDataGlobal.isEmpty()) {
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.toString(), sku);
        }
        if (itemDataGlobal.size() > 1) {
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.toString(), sku);
        }*/
        String warehouseId = applicationContext.getCurrentWarehouse();
        List<ItemData> itemDataList = itemDataRepository.getBysku(sku,warehouseId);
        if(itemDataList.isEmpty()){
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.getName(), sku);
        }
        if(itemDataList.size() > 1){
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.getName(), sku);
        }
    }

    public void checkStorageLocationWeight(UnitLoad unitLoad, ItemData itemData, BigDecimal amount) {
        // 超过货位的载重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(amount);
        BigDecimal weight = unitLoad.getWeightCalculated().add(itemDataWeight);
        BigDecimal storageLocationWeight = unitLoad.getStorageLocation().getStorageLocationType().getLiftingCapacity();
        if (storageLocationWeight.compareTo(weight) == -1) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_WEIGHT_HAS_MAX.getName(), unitLoad.getStorageLocation().getName()+"达到最大承重");
        }
    }

    public void checkStorageLocationSimilar(UnitLoad unitLoad, ItemData itemData){
        // 货位中存在相似商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(unitLoad, itemData);
        checkSemblenceInfo(destinationStockUnits, itemData);

    }

    public void checkStorageLocationItemsAmount(UnitLoad unitLoad, ItemData itemData) {
        // 商品种类超过系统设置数量
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
        if (stockUnits.isEmpty()) {
            long itemDataAmount = stockUnitRepository.countByUnitLoad(unitLoad) + 1;
            if (itemDataAmount
                    > unitLoad.getStorageLocation().getStorageLocationType().getMaxItemDataAmount()) {
                throw new ApiException(InternalToolException.
                        EX_IT_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT.getName(),
                        unitLoad.getStorageLocation().getName()+"商品种类超过系统设置数量");
            }
        }
    }

    public void checkStorageLocationItemGroup(StorageLocation storageLocation, ItemData itemData) {
        // 商品属性与货位设置不符
        Set<ItemGroup> itemGroups = storageLocation.getZone().getItemGroups();
        boolean useItemGroupFlag = false;
        for (ItemGroup itemGroup : itemGroups) {
            if (itemGroup.getId().equalsIgnoreCase(itemData.getItemGroup().getId())) {
                useItemGroupFlag = true;
                break;
            }
        }
        if (!useItemGroupFlag) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP.getName(),
                    storageLocation.getName());
        }
    }

    public void checkStorageLocationLot(UnitLoad unitLoad, ItemData itemData, Lot lot) {
        if (itemData.isLotMandatory()) {
            // 相同商品不同有效期商品
            if (unitLoad != null) {
                List<StockUnit> stockUnit = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
                if (!stockUnit.isEmpty() && stockUnit.get(0).getAmount().compareTo(BigDecimal.ZERO) == 1) {
                    if (!stockUnit.get(0).getLot().getId().equalsIgnoreCase(lot.getId())) {
                        throw new ApiException(InternalToolException
                                .EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_LOT.getName(),
                                unitLoad.getStorageLocation().getName());
                    }
                }
            }
        }
    }

    public void checkStorageLocationClient(UnitLoad unitLoad, ItemData itemData) {
        // 不同客户的相同商品是不容许放入同一个货位中
        StockUnit stockUnit = stockUnitRepository.getByUnequalClientAndItemNo(
                unitLoad, itemData.getClient().getId(), itemData.getItemNo());
        if (stockUnit != null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT.getName(),
                    unitLoad.getStorageLocation().getName());
        }
    }

    public void checkingUsername(String username) {
        String sql = "SELECT U.ID FROM SYS_USER U WHERE U.USERNAME = '" + username + "'";
        Query query = entityManager.createNativeQuery(sql);
        try {
            Object result = query.getSingleResult();
            if (result == null) {
                throw new ApiException(InternalToolException.EX_IT_USERNAME_NOT_FOUND.getName(), username);
            }
        } catch (Exception e) {
            throw new ApiException(InternalToolException.EX_IT_USERNAME_NOT_FOUND.getName(), username);
        }
    }

    //检查商品的相似度
    public void checkSemblenceInfo(List<StockUnit> stockUnitList,ItemData itemData){
        log.info("需要比较的相似度商品:"+itemData.getName());
        Semblence semblence=semblenceRepository.getSemblence();
        for (StockUnit stockUnit:stockUnitList){
            float seemValue = CommonUtil.semilar(stockUnit.getItemData().getName(),itemData.getName());//取两个名字的相似程度
            log.info(stockUnit.getItemData().getItemNo()+"与"+itemData.getItemNo()+"的相似值--->"+seemValue);
            if(seemValue<1&&seemValue>((float)semblence.getSemblence()/(float)100))
                throw new ApiException(InternalToolException.EX_SEMBLENCE_SKU.getName());
            if(seemValue==1&&!stockUnit.getItemData().getId().equalsIgnoreCase(itemData.getId()))
                throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.getName());
        }
    }
}
