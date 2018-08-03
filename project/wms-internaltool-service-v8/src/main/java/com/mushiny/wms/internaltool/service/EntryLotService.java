package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.web.dto.EntryLotDTO;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;

import java.math.BigDecimal;

public interface EntryLotService {

    StorageLocationAmountDTO scanningSource(String sourceName);

    ItemDataAmountDTO scanningItemData(String sourceId, String sku);

    BigDecimal entering(EntryLotDTO entryLotDTO);
}
