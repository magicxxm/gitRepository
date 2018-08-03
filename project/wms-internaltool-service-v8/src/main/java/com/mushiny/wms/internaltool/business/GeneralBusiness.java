package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.StockUnit;
import com.mushiny.wms.internaltool.common.domain.StorageLocation;
import com.mushiny.wms.internaltool.common.domain.UnitLoad;
import com.mushiny.wms.internaltool.common.enums.StockUnitState;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class GeneralBusiness {

    private final StockUnitRepository stockUnitRepository;

    @Autowired
    public GeneralBusiness(StockUnitRepository stockUnitRepository) {
        this.stockUnitRepository = stockUnitRepository;
    }

    public String getInventoryState(StorageLocation storageLocation) {
        String inventoryState = storageLocation.getStorageLocationType().getInventoryState();
        switch (inventoryState) {
            case "DAMAGE":
                return StockUnitState.DAMAGE.getName();
            case "PENDING":
                return StockUnitState.PENDING.getName();
            default:
                return StockUnitState.INVENTORY.getName();
        }
    }

    public BigDecimal getStockUnitAmount(UnitLoad unitLoad) {
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoad(unitLoad);
        BigDecimal amount = BigDecimal.ZERO;
        for (StockUnit stockUnit : stockUnits) {
            amount = amount.add((stockUnit.getAmount().subtract(stockUnit.getReservedAmount())));
        }
        return amount;
    }

    public BigDecimal getStockUnitAmount(UnitLoad unitLoad, ItemData itemData) {
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
        BigDecimal amount = BigDecimal.ZERO;
        for (StockUnit stockUnit : stockUnits) {
            amount = amount.add((stockUnit.getAmount().subtract(stockUnit.getReservedAmount())));
        }
        return amount;
    }
}
