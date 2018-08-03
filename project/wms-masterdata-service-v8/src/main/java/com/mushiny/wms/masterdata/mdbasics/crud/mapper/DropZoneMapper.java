package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.DropZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.DropZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DropZoneMapper implements BaseMapper<DropZoneDTO, DropZone> {

    private final ApplicationContext applicationContext;;

    @Autowired
    public DropZoneMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public DropZoneDTO toDTO(DropZone entity) {
        if (entity == null) {
            return null;
        }

        DropZoneDTO dto = new DropZoneDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStandardTime(entity.getStandardTime());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouseId()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public DropZone toEntity(DropZoneDTO dto) {
        if (dto == null) {
            return null;
        }

        DropZone entity = new DropZone();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStandardTime(dto.getStandardTime());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(DropZoneDTO dto, DropZone entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStandardTime(dto.getStandardTime());
    }
}

