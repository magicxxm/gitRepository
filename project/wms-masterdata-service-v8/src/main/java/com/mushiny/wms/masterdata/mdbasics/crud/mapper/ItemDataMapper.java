package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataGlobal;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataGlobalRepository;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDataMapper implements BaseMapper<ItemDataDTO, ItemData> {

    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ApplicationContext applicationContext;
    private final ItemUnitMapper itemUnitMapper;
    private final ItemGroupMapper itemGroupMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ItemDataMapper(
            ApplicationContext applicationContext,
            ItemDataGlobalRepository itemDataGlobalRepository,
            ItemGroupMapper itemGroupMapper,
            ItemUnitMapper itemUnitMapper,
            ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.itemGroupMapper = itemGroupMapper;
        this.itemUnitMapper = itemUnitMapper;
        this.clientRepository = clientRepository;
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

        dto.setItemGroup(itemGroupMapper.toDTO(entity.getItemGroup()));
        dto.setItemUnit(itemUnitMapper.toDTO(entity.getItemUnit()));
        dto.setItemSellingDegree(entity.getItemSellingDegree());
        dto.setLotThreshold(entity.getLotThreshold());

        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setSize(entity.getSize());
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
        entity.setItemGroup(itemDataGlobal.getItemGroup());
        entity.setItemUnit(itemDataGlobal.getItemUnit());
        entity.setLotType(itemDataGlobal.getLotType());
        entity.setLotUnit(itemDataGlobal.getLotUnit());
        entity.setLotThreshold(itemDataGlobal.getLotThreshold());
        entity.setSize(itemDataGlobal.getSize());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setItemSellingDegree(dto.getItemSellingDegree());
        entity.setItemPicture(itemDataGlobal.getItemPicture());
        entity.setPictureName(itemDataGlobal.getPictureName());

        return entity;
    }

    public ItemData importToEntity(ItemDataDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemData entity = new ItemData();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setItemDataGlobalId(dto.getItemDataGlobalId());

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
        entity.setItemGroup(itemDataGlobal.getItemGroup());
        entity.setItemUnit(itemDataGlobal.getItemUnit());
        entity.setLotType(itemDataGlobal.getLotType());
        entity.setLotUnit(itemDataGlobal.getLotUnit());
        entity.setLotThreshold(itemDataGlobal.getLotThreshold());

        entity.setName(itemDataGlobal.getName());
        entity.setDescription(itemDataGlobal.getDescription());
        entity.setSafetyStock(itemDataGlobal.getSafetyStock());
        entity.setSize(itemDataGlobal.getSize());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setItemSellingDegree(dto.getItemSellingDegree());
        entity.setItemPicture(itemDataGlobal.getItemPicture());
        entity.setPictureName(itemDataGlobal.getPictureName());

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

