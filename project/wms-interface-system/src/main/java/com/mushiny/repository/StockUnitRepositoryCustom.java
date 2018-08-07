package com.mushiny.repository;

import com.mushiny.model.StockUnit;
import com.mushiny.model.UnitLoad;
import com.mushiny.model.Warehouse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by 123 on 2018/2/8.
 */
public interface StockUnitRepositoryCustom {

    BigDecimal getAmountByUnitLoad(UnitLoad unitLoad, Warehouse warehouse);

    List<StockUnit> getStockUnitList(String clientId, LocalDate endDate, String itemNo, String stockState,String batchNo,String warehouseId);
}
