package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.StockUnit;
import com.mushiny.wms.internaltool.web.dto.*;

import java.util.List;

public interface AdjustInventoryService {

    StorageLocationAmountDTO scanningSource(String sourceName);

    List<StockUnit> getStorageLocationItemData(String storageLocationId);

    ItemDataAmountDTO scanningItemData(String sourceId, String sku);

    ItemDataGlobalAmountDTO scanningItemDataGlobal(String sourceId, String sku);

    StorageLocationAmountDTO scanningDestination(String sourceId, String itemDataId, String destinationName);

    void checkUser(String username);

    void updateInventoryAttributes(InventoryAttributesDTO inventoryAttributesDTO);

    void moveAllGoods(MoveGoodsDTO moveGoodsDTO);

    void moveGoods(MoveGoodsDTO moveGoodsDTO);

    void overageGoods(OverageGoodsDTO overageGoodsDTO);

    void lossGoods(LossGoodsDTO lossGoodsDTO);
}
