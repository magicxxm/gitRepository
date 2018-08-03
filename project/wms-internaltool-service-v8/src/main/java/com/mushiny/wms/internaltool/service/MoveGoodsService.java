package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MoveGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;

public interface MoveGoodsService {

    StorageLocationAmountDTO scanningSource(String sourceName);

    ItemDataAmountDTO scanningItemData(String sourceId, String sku);

    StorageLocationAmountDTO scanningDestination(String sourceId, String itemDataId, String destinationName);

    void moving(MoveGoodsDTO moveGoodsDTO);
}
