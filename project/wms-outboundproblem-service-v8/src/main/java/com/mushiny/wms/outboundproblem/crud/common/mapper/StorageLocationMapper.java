package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.repository.common.StorageLocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageLocationMapper implements BaseMapper<StorageLocationDTO, StorageLocation> {

    private final StorageLocationTypeRepository storageLocationTypeRepository;

    @Autowired
    public StorageLocationMapper(StorageLocationTypeRepository storageLocationTypeRepository) {
        this.storageLocationTypeRepository = storageLocationTypeRepository;
    }


    @Override
    public StorageLocationDTO toDTO(StorageLocation entity) {
        if (entity == null) {
            return null;
        }

        StorageLocationDTO dto = new StorageLocationDTO(entity);

        dto.setName(entity.getName());
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setzPos(entity.getzPos());
        dto.setField(entity.getField());
        dto.setFieldIndex(entity.getFieldIndex());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setAllocation(entity.getAllocation());
        dto.setAllocationState(entity.getAllocationState());
        dto.setStocktakingDate(entity.getStocktakingDate());
        dto.setFace(entity.getFace());
        dto.setColor(entity.getColor());

        return dto;
    }

    @Override
    public StorageLocation toEntity(StorageLocationDTO dto) {
        if (dto == null) {
            return null;
        }

        StorageLocation entity = new StorageLocation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setField(dto.getField());
        entity.setFieldIndex(dto.getFieldIndex());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setStorageLocationType(storageLocationTypeRepository.retrieve(dto.getTypeId()));
        entity.setAllocation(dto.getAllocation());
        entity.setAllocationState(dto.getAllocationState());
        entity.setStocktakingDate(dto.getStocktakingDate());
        entity.setFace(dto.getFace());
        entity.setColor(dto.getColor());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StorageLocationDTO dto, StorageLocation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setField(dto.getField());
        entity.setFieldIndex(dto.getFieldIndex());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setAllocation(dto.getAllocation());
        entity.setAllocationState(dto.getAllocationState());
        entity.setStocktakingDate(dto.getStocktakingDate());
        entity.setColor(dto.getColor());
    }
}

