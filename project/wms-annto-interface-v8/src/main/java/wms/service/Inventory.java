package wms.service;

import wms.common.crud.InventoryAccessDTO;
import wms.crud.dto.InventoryGetDTO;

import java.util.List;

public interface Inventory {

    InventoryAccessDTO getInventory(String warehouseCode, String itemCode);

}
