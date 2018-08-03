package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataGlobalDTO;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataGlobal;
//import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
//import com.mushiny.wms.masterdata.mdbasics.repository.ItemUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDataGlobalMapper implements BaseMapper<ItemDataGlobalDTO, ItemDataGlobal> {


//    private final ItemUnitRepository itemUnitRepository;
//    private final ItemGroupRepository itemGroupRepository;
//    private final ItemUnitMapper itemUnitMapper;
//    private final ItemGroupMapper itemGroupMapper;

    @Autowired
    public ItemDataGlobalMapper() {
    }

    @Override
    public ItemDataGlobalDTO toDTO(ItemDataGlobal entity) {
        if (entity == null) {
            return null;
        }

        ItemDataGlobalDTO dto = new ItemDataGlobalDTO(entity);

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
        dto.setLotThreshold(entity.getLotThreshold());
//        dto.setItemGroup(itemGroupMapper.toDTO(entity.getItemGroup()));
//        dto.setItemUnit(itemUnitMapper.toDTO(entity.getItemUnit()));

        return dto;
    }

    @Override
    public ItemDataGlobal toEntity(ItemDataGlobalDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemDataGlobal entity = new ItemDataGlobal();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setItemNo(dto.getItemNo());
        entity.setSkuNo(dto.getSkuNo());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSafetyStock(dto.getSafetyStock());
        entity.setLotMandatory(dto.isLotMandatory());
        entity.setSerialRecordType(dto.getSerialRecordType());
        entity.setSerialRecordLength(dto.getSerialRecordLength());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setWeight(dto.getWeight());
        entity.setMultiplePart(dto.isMultiplePart());
        entity.setMultiplePartAmount(dto.getMultiplePartAmount());
        entity.setMeasured(dto.isMeasured());
        entity.setPreferOwnBox(dto.isPreferOwnBox());
        entity.setPreferBag(dto.isPreferBag());
        entity.setUseBubbleFilm(dto.isUseBubbleFilm());
        entity.setLotType(dto.getLotType());
        entity.setLotUnit(dto.getLotUnit());
        entity.setLotThreshold(dto.getLotThreshold());

//        if (dto.getItemGroupId() != null) {
//            entity.setItemGroup(itemGroupRepository.retrieve(dto.getItemGroupId()));
//        }
//        if (dto.getHandlingUnitId() != null) {
//            entity.setItemUnit(itemUnitRepository.retrieve(dto.getHandlingUnitId()));
//        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemDataGlobalDTO dto, ItemDataGlobal entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSafetyStock(dto.getSafetyStock());
        entity.setLotMandatory(dto.isLotMandatory());
        entity.setSerialRecordType(dto.getSerialRecordType());
        entity.setSerialRecordLength(dto.getSerialRecordLength());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setWeight(dto.getWeight());
        entity.setMultiplePart(dto.isMultiplePart());
        entity.setMultiplePartAmount(dto.getMultiplePartAmount());
        entity.setMeasured(dto.isMeasured());
        entity.setPreferOwnBox(dto.isPreferOwnBox());
        entity.setPreferBag(dto.isPreferBag());
        entity.setUseBubbleFilm(dto.isUseBubbleFilm());
        entity.setLotType(dto.getLotType());
        entity.setLotUnit(dto.getLotUnit());
        entity.setLotThreshold(dto.getLotThreshold());

//        if (dto.getItemGroupId() != null) {
//            entity.setItemGroup(itemGroupRepository.retrieve(dto.getItemGroupId()));
//        }
//        if (dto.getHandlingUnitId() != null) {
//            entity.setItemUnit(itemUnitRepository.retrieve(dto.getHandlingUnitId()));
//        }
    }

    public void updateItemDataFromItemDataGlobal(ItemData itemData, ItemDataGlobal itemDataGlobal) {
        if (itemDataGlobal == null || itemData == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        itemData.setLotMandatory(itemDataGlobal.isLotMandatory());
        itemData.setSerialRecordType(itemDataGlobal.getSerialRecordType());
        itemData.setSerialRecordLength(itemDataGlobal.getSerialRecordLength());
        itemData.setHeight(itemDataGlobal.getHeight());
        itemData.setWidth(itemDataGlobal.getWidth());
        itemData.setDepth(itemDataGlobal.getDepth());
        itemData.setVolume(itemDataGlobal.getVolume());
        itemData.setWeight(itemDataGlobal.getWeight());
        itemData.setMultiplePart(itemDataGlobal.isMultiplePart());
        itemData.setMultiplePartAmount(itemDataGlobal.getMultiplePartAmount());
        itemData.setMeasured(itemDataGlobal.isMeasured());
        itemData.setPreferOwnBox(itemDataGlobal.isPreferOwnBox());
        itemData.setPreferBag(itemDataGlobal.isPreferBag());
        itemData.setUseBubbleFilm(itemDataGlobal.isUseBubbleFilm());
//        itemData.setItemGroup(itemDataGlobal.getItemGroup());
//        itemData.setItemUnit(itemDataGlobal.getItemUnit());

        itemData.setLotType(itemDataGlobal.getLotType());
        itemData.setLotUnit(itemDataGlobal.getLotUnit());
        itemData.setLotThreshold(itemDataGlobal.getLotThreshold());
//        itemData.setLotType(itemDataGlobal.getLotType());
    }
}
