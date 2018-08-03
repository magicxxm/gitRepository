package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.crud.dto.PropertyDTO;
import com.mushiny.wms.system.domain.Property;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper implements BaseMapper<PropertyDTO, Property> {

    private final ApplicationContext applicationContext;

    public PropertyMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public PropertyDTO toDTO(Property entity) {
        if (entity == null) {
            return null;
        }
        PropertyDTO dto = new PropertyDTO(entity);
        dto.setDescription(entity.getDescription());
        dto.setSystemKey(entity.getSystemKey());
        dto.setSystemValue(entity.getSystemValue());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Property toEntity(PropertyDTO dto) {
        if (dto == null) {
            return null;
        }
        Property entity = new Property();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setDescription(dto.getDescription());
        entity.setSystemKey(dto.getSystemKey());
        entity.setSystemValue(dto.getSystemValue());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return null;
    }

    @Override
    public void updateEntityFromDTO(PropertyDTO dto, Property entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setDescription(dto.getDescription());
        entity.setSystemKey(dto.getSystemKey());
        entity.setSystemValue(dto.getSystemValue());
    }
}
