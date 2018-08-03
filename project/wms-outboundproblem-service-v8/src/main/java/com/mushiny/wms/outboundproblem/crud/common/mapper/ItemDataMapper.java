package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataDTO;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataGlobal;
import com.mushiny.wms.outboundproblem.repository.common.ItemDataGlobalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDataMapper implements BaseMapper<ItemDataDTO, ItemData> {

    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public ItemDataMapper(
                ApplicationContext applicationContext,
                ItemDataGlobalRepository itemDataGlobalRepository) {
        this.applicationContext = applicationContext;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
    }

    @Override
    public ItemDataDTO toDTO(ItemData entity) {
        if (entity == null) {
            return null;
        }

        ItemDataDTO dto = new ItemDataDTO(entity);

        dto.setItemDataGlobalId(entity.getItemDataGlobalId());

        dto.setItemNo(entity.getItemNo());
        dto.setSkuNo(entity.getSkuNo());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSafetyStock(entity.getSafetyStock());
        dto.setLotMandatory(entity.isLotMandatory());
        dto.setSerialRecordType(entity.getSerialRecordType());
        dto.setSerialRecordLength(entity.getSerialRecordLength());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setVolume(entity.getVolume());
        dto.setWeight(entity.getWeight());
        dto.setMultiplePart(entity.isMultiplePart());
        dto.setMultiplePartAmount(entity.getMultiplePartAmount());
        dto.setMeasured(entity.isMeasured());
        dto.setPreferOwnBox(entity.isPreferOwnBox());
        dto.setPreferBag(entity.isPreferBag());
        dto.setUseBubbleFilm(entity.isUseBubbleFilm());
        dto.setLotType(entity.getLotType());
        dto.setLotUnit(entity.getLotUnit());

        dto.setItemSellingDegree(entity.getItemSellingDegree());
        dto.setLotThreshold(entity.getLotThreshold());

        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ItemData toEntity(ItemDataDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemData entity = new ItemData();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setItemDataGlobalId(dto.getItemDataGlobalId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSafetyStock(dto.getSafetyStock());

        ItemDataGlobal itemDataGlobal = itemDataGlobalRepository.retrieve(dto.getItemDataGlobalId());
        entity.setItemNo(itemDataGlobal.getItemNo());
        entity.setSkuNo(itemDataGlobal.getSkuNo());
        entity.setLotMandatory(itemDataGlobal.isLotMandatory());
        entity.setSerialRecordType(itemDataGlobal.getSerialRecordType());
        entity.setSerialRecordLength(itemDataGlobal.getSerialRecordLength());
        entity.setHeight(itemDataGlobal.getHeight());
        entity.setWidth(itemDataGlobal.getWidth());
        entity.setDepth(itemDataGlobal.getDepth());
        entity.setVolume(itemDataGlobal.getVolume());
        entity.setWeight(itemDataGlobal.getWeight());
        entity.setMultiplePart(itemDataGlobal.isMultiplePart());
        entity.setMultiplePartAmount(itemDataGlobal.getMultiplePartAmount());
        entity.setMeasured(itemDataGlobal.isMeasured());
        entity.setPreferOwnBox(itemDataGlobal.isPreferOwnBox());
        entity.setPreferBag(itemDataGlobal.isPreferBag());
        entity.setUseBubbleFilm(itemDataGlobal.isUseBubbleFilm());
        entity.setLotType(itemDataGlobal.getLotType());
        entity.setLotUnit(itemDataGlobal.getLotUnit());
        entity.setLotThreshold(itemDataGlobal.getLotThreshold());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        entity.setItemSellingDegree(dto.getItemSellingDegree());


        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemDataDTO dto, ItemData entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSafetyStock(dto.getSafetyStock());

        entity.setItemSellingDegree(dto.getItemSellingDegree());
        entity.setItemSellingDegree(dto.getItemSellingDegree());
//        entity.setLotType(dto.getLotType());
//        entity.setLotUnit(dto.getLotUnit());
        entity.setLotThreshold(dto.getLotThreshold());
    }
}

