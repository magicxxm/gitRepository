package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliverySortCodeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import org.springframework.stereotype.Component;

@Component
public class DeliverySortCodeMapper implements BaseMapper<DeliverySortCodeDTO,DeliverySortCode> {
    private final ApplicationContext applicationContext;
    public DeliverySortCodeMapper(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    @Override
    public DeliverySortCodeDTO toDTO(DeliverySortCode entity) {
        if (entity == null) {
            return null;
        }
        DeliverySortCodeDTO dto = new DeliverySortCodeDTO(entity);
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public DeliverySortCode toEntity(DeliverySortCodeDTO dto) {
        if (dto == null) {
            return null;
        }
        DeliverySortCode entity = new DeliverySortCode();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(DeliverySortCodeDTO dto, DeliverySortCode entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
    }
}