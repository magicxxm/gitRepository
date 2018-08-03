package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;
import com.mushiny.wms.masterdata.ibbasics.repository.UnitLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitLoadBusiness {

    private final UnitLoadRepository unitLoadRepository;

    @Autowired
    public UnitLoadBusiness(UnitLoadRepository unitLoadRepository) {
        this.unitLoadRepository = unitLoadRepository;
    }

    public UnitLoad buildAndSave(StockUnit stockUnit) {
        UnitLoad unitLoad = new UnitLoad();
        boolean useFlag = true;
        while (useFlag) {
            String label = RandomUtil.getUnitLoadLabel();
            UnitLoad useUnitLoad = unitLoadRepository.getByLabel(label);
            if (useUnitLoad == null) {
                unitLoad.setLabel(label);
                useFlag = false;
            }
        }
        unitLoad.setStockTakingDate(DateTimeUtil.getNowDateTime());
//        unitLoad.setStorageLocation(stockUnit.getStorageLocation());
//        unitLoad.setContainer(stockUnit.getContainer());
//        unitLoad.setOpened(false);
//        if (stockUnit.getStorageLocation() == null) {
//            unitLoad.setLocationIndex(0);
//        } else {
//            if (stockUnit.getStorageLocation().getBay() == null) {
//                unitLoad.setLocationIndex(0);
//            } else {
//                unitLoad.setLocationIndex(stockUnit.getStorageLocation().getBay().getBayIndex());
//            }
//        }

        unitLoad.setCarrier(false);
        unitLoad.setClientId(stockUnit.getClientId());
        unitLoad.setWarehouseId(stockUnit.getWarehouseId());
        return unitLoadRepository.save(unitLoad);
    }
}
