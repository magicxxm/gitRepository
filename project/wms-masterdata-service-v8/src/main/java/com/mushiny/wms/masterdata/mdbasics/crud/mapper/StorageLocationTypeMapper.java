package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageLocationTypeMapper implements BaseMapper<StorageLocationTypeDTO, StorageLocationType> {

    private final ApplicationContext applicationContext;

    @Autowired
    public StorageLocationTypeMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public StorageLocationTypeDTO toDTO(StorageLocationType entity) {
        if (entity == null) {
            return null;
        }

        StorageLocationTypeDTO dto = new StorageLocationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setVolume(entity.getVolume());
        dto.setLiftingCapacity(entity.getLiftingCapacity());
        dto.setMaxItemDataAmount(entity.getMaxItemDataAmount());

        dto.setInventoryState(entity.getInventoryState());
        dto.setStorageType(entity.getStorageType());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouseId()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public StorageLocationType toEntity(StorageLocationTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        StorageLocationType entity = new StorageLocationType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getVolume());
        entity.setLiftingCapacity(dto.getLiftingCapacity());
        entity.setMaxItemDataAmount(dto.getMaxItemDataAmount());
        entity.setInvertoryState(dto.getInventoryState());
        entity.setStorageType(dto.getStorageType());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StorageLocationTypeDTO dto, StorageLocationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getVolume());
        entity.setLiftingCapacity(dto.getLiftingCapacity());
        entity.setMaxItemDataAmount(dto.getMaxItemDataAmount());
        entity.setInvertoryState(dto.getInventoryState());
        entity.setStorageType(dto.getStorageType());
    }
}

