package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;
import com.mushiny.wms.internaltool.common.repository.ItemDataGlobalRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.service.PrintBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrintBarcodeServiceImpl implements PrintBarcodeService {

    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public PrintBarcodeServiceImpl(ItemDataGlobalRepository itemDataGlobalRepository,
                                   ApplicationContext applicationContext,
                                   ItemDataRepository itemDataRepository) {
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.itemDataRepository= itemDataRepository;
        this.applicationContext= applicationContext;
    }

    @Override
    public List<ItemData> scanningSKU(String sku) {
        /*List<ItemDataGlobal> itemDataGlobals = itemDataGlobalRepository.getBySKU(sku);
        if (itemDataGlobals.isEmpty()) {
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.toString(), sku);
        }*/

        List<ItemData> itemDataList = itemDataRepository.getBysku(sku,applicationContext.getCurrentWarehouse());
        if(itemDataList.isEmpty()){
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.getName(), sku);
        }

        return itemDataList;
    }
}
