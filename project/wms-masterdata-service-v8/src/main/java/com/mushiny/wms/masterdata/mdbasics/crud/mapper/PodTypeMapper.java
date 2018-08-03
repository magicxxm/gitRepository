package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PodTypeMapper implements BaseMapper<PodTypeDTO, PodType> {

    private final ApplicationContext applicationContext;

    @Autowired
    public PodTypeMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public PodTypeDTO toDTO(PodType entity) {
        if (entity == null) {
            return null;
        }

        PodTypeDTO dto = new PodTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouse()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PodType toEntity(PodTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PodType entity = new PodType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PodTypeDTO dto, PodType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
    }
}
