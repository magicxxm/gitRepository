package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.business.BuildBusiness;
import com.mushiny.wms.internaltool.business.ScanningBusiness;
import com.mushiny.wms.internaltool.business.UnitLoadBusiness;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.common.repository.StorageLocationRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.service.EntryLotService;
import com.mushiny.wms.internaltool.web.dto.EntryLotDTO;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class EntryLotServiceImpl implements EntryLotService {

    private final ScanningBusiness scanningBusiness;
    private final StorageLocationRepository storageLocationRepository;
    private final StockUnitRepository stockUnitRepository;
    private final ItemDataRepository itemDataRepository;
    private final UnitLoadBusiness unitLoadBusiness;
    private final BuildBusiness buildBusiness;

    @Autowired
    public EntryLotServiceImpl(ItemDataRepository itemDataRepository,
                               StorageLocationRepository storageLocationRepository,
                               StockUnitRepository stockUnitRepository,
                               ScanningBusiness scanningBusiness,
                               BuildBusiness buildBusiness,
                               UnitLoadBusiness unitLoadBusiness) {
        this.itemDataRepository = itemDataRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.scanningBusiness = scanningBusiness;
        this.buildBusiness = buildBusiness;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    @Override
    public StorageLocationAmountDTO scanningSource(String sourceName) {
        return scanningBusiness.getStorageLocationAmount(sourceName);
    }

    @Override
    public ItemDataAmountDTO scanningItemData(String sourceId, String sku) {
        return scanningBusiness.getItemDataAmounts(sourceId, sku);
    }

    @Override
    public BigDecimal entering(EntryLotDTO entryLotDTO) {
        StorageLocation source = storageLocationRepository.retrieve(entryLotDTO.getSourceId());
        ItemData itemData = itemDataRepository.retrieve(entryLotDTO.getItemDataId());
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        if (sourceStockUnits.isEmpty()) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        Lot sourceLot = sourceStockUnits.get(0).getLot();
        Lot lot = buildBusiness.buildLot(itemData, entryLotDTO.getUseNotAfter());
        BigDecimal amount = BigDecimal.ZERO;
        for (StockUnit sourceStockUnit : sourceStockUnits) {
            amount = amount.add(sourceStockUnit.getAmount());
            sourceStockUnit.setLot(lot);
        }
        stockUnitRepository.save(sourceStockUnits);
        buildBusiness.buildLotRecord(itemData, sourceLot, lot, source, amount);
        return amount;
    }
}
