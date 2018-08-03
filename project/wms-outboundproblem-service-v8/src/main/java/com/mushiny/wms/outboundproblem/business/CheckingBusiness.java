package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.common.ItemDataGlobalRepository;
import com.mushiny.wms.outboundproblem.repository.common.StockUnitRepository;
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
    @Autowired
    public CheckingBusiness(ItemDataGlobalRepository itemDataGlobalRepository,
                            StockUnitRepository stockUnitRepository) {
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.stockUnitRepository = stockUnitRepository;
    }
    public boolean isBin(StorageLocation storageLocation) {
        boolean bool = false;
        if (storageLocation.getStorageLocationType()!= null){
            bool = storageLocation.getStorageLocationType().getStorageType().equalsIgnoreCase("BIN");
        }
        return bool;
    }

    public void checkingUnitLoadEntityLock(UnitLoad unitLoad) {
        if (unitLoad.getEntityLock() > 1) {
            throw new ApiException(OutBoundProblemException.EX_IT_STORAGE_LOCATION_NOT_USED.getName());
        }
    }

    public void checkStorageLocationWeight(UnitLoad unitLoad, ItemData itemData, BigDecimal amount) {
        // 超过货位的载重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(amount);
        BigDecimal weight = unitLoad.getWeightCalculated().add(itemDataWeight);
        BigDecimal storageLocationWeight = unitLoad.getStorageLocation().getStorageLocationType().getLiftingCapacity();
        if (storageLocationWeight.compareTo(weight) == -1) {
            throw new ApiException(OutBoundProblemException
                    .EX_IT_STORAGE_LOCATION_WEIGHT_HAS_MAX.getName(), unitLoad.getStorageLocation().getName());
        }
    }
    public void checkStorageLocationSimilar(UnitLoad unitLoad, ItemData itemData) {
        // 货位中存在相似商品
    }
    public void checkStorageLocationItemsAmount(UnitLoad unitLoad, ItemData itemData) {
        // 商品种类超过系统设置数量
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
        if (stockUnits.isEmpty()) {
            long itemDataAmount = stockUnitRepository.countByUnitLoad(unitLoad) + 1;
            if (itemDataAmount
                    > unitLoad.getStorageLocation().getStorageLocationType().getMaxItemDataAmount()) {
                throw new ApiException(OutBoundProblemException.
                        EX_IT_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT.getName(),
                        unitLoad.getStorageLocation().getName());
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
            }
        }
        if (!useItemGroupFlag) {
            throw new ApiException(OutBoundProblemException
                    .EX_IT_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP.getName(),
                    storageLocation.getName());
        }
    }
    public void checkStorageLocationLot(UnitLoad unitLoad, ItemData itemData, Lot lot) {
        if (itemData.isLotMandatory()) {
            // 相同商品不同有效期商品
            if (unitLoad != null) {
                List<StockUnit> stockUnit = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
                if (lot!=null&&!stockUnit.isEmpty() && stockUnit.get(0).getAmount().compareTo(BigDecimal.ZERO) == 1) {
                    if (!stockUnit.get(0).getLot().getId().equalsIgnoreCase(lot.getId())) {
                        throw new ApiException(OutBoundProblemException
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
                unitLoad, itemData.getClientId(), itemData.getItemNo());
        if (stockUnit != null) {
            throw new ApiException(OutBoundProblemException
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
                throw new ApiException(OutBoundProblemException.EX_IT_USERNAME_NOT_FOUND.getName(), username);
            }
        } catch (Exception e) {
            throw new ApiException(OutBoundProblemException.EX_IT_USERNAME_NOT_FOUND.getName(), username);
        }
    }
}
