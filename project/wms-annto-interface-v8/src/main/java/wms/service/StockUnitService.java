package wms.service;

import wms.common.crud.AccessDTO;
import wms.common.crud.InventoryAccessDTO;
import wms.crud.dto.StockUnitCheckDTO;

/**
 * Created by 123 on 2017/8/10.
 */
public interface StockUnitService {

    InventoryAccessDTO getStock(String warehouseCode, String customerCode, String itemCode, String inventoryType, String page, String pageNo);

    AccessDTO getStockUnit(StockUnitCheckDTO stockUnitCheckDTO);

    InventoryAccessDTO acceptStockUnit(StockUnitCheckDTO stockUnitCheckDTO);
}
