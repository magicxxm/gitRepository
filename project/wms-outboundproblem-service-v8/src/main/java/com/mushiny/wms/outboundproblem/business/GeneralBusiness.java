package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.outboundproblem.business.enums.StockUnitState;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.StockUnit;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.common.UnitLoad;
import com.mushiny.wms.outboundproblem.repository.common.StockUnitRepository;
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
        if(storageLocation.getStorageLocationType()!= null){
            String inventoryState = storageLocation.getStorageLocationType().getInventoryState();
            switch (inventoryState) {
                case "DAMAGE":
                    return StockUnitState.DAMAGE.getName();
                case "PENDING":
                    return StockUnitState.PENDING.getName();
                default:
                    return StockUnitState.INVENTORY.getName();
            }
        }else{
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
