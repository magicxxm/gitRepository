package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPathType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessPathTypeMapper implements BaseMapper<ProcessPathTypeDTO, ProcessPathType> {

    private final ApplicationContext applicationContext;

    @Autowired
    public ProcessPathTypeMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ProcessPathTypeDTO toDTO(ProcessPathType entity) {
        if (entity == null) {
            return null;
        }
        ProcessPathTypeDTO dto = new ProcessPathTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPickFlow(entity.getPickFlow());
        dto.setPickWay(entity.getPickWay());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ProcessPathType toEntity(ProcessPathTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        ProcessPathType entity = new ProcessPathType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPickFlow(dto.getPickFlow());
        entity.setPickWay(dto.getPickWay());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ProcessPathTypeDTO dto, ProcessPathType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setPickFlow(dto.getPickFlow());
        entity.setPickWay(dto.getPickWay());
        entity.setDescription(dto.getDescription());
    }
}
