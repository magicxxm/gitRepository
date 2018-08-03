package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MeasureGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;

public interface MeasureGoodsService {

    StorageLocationAmountDTO scanningSource(String sourceName);

    ItemDataAmountDTO scanningItemData(String sourceId, String sku);

    StorageLocationAmountDTO scanningDestination(String sourceId, String itemDataId, String destinationName);

    void measuring(MeasureGoodsDTO measureGoodsDTO);
}
