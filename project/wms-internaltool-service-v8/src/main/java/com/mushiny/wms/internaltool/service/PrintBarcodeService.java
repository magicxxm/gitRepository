package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;

import java.util.List;

public interface PrintBarcodeService {

    List<ItemData> scanningSKU(String sku);
}
