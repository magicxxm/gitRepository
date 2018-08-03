package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.internaltool.business.MoveGoodsBusiness;
import com.mushiny.wms.internaltool.business.ScanningBusiness;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.StorageLocation;
import com.mushiny.wms.internaltool.common.enums.StockUnitRecordState;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.StorageLocationRepository;
import com.mushiny.wms.internaltool.service.MoveGoodsService;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MoveGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MoveGoodsServiceImpl implements MoveGoodsService {

    private final ScanningBusiness scanningBusiness;
    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final MoveGoodsBusiness moveGoodsBusiness;

    @Autowired
    public MoveGoodsServiceImpl(ItemDataRepository itemDataRepository,
                                StorageLocationRepository storageLocationRepository,
                                ScanningBusiness scanningBusiness,
                                MoveGoodsBusiness moveGoodsBusiness) {
        this.itemDataRepository = itemDataRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.scanningBusiness = scanningBusiness;
        this.moveGoodsBusiness = moveGoodsBusiness;
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
    public StorageLocationAmountDTO scanningDestination(String sourceId,
                                                        String itemDataId,
                                                        String destinationName) {
        return scanningBusiness.getStorageLocationAmount(sourceId, itemDataId, destinationName);
    }

    @Override
    public void moving(MoveGoodsDTO moveGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(moveGoodsDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(moveGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.retrieve(moveGoodsDTO.getItemDataId());
        moveGoodsBusiness.moving(source, destination, itemData, moveGoodsDTO.getAmount(),
                StockUnitRecordState.MOVE_GOODS_RECORD_CODE.getName(),
                StockUnitRecordState.MOVE_GOODS_RECORD_TOOL.getName(),
                StockUnitRecordState.MOVE_GOODS_RECORD_TYPE.getName());
    }
}
