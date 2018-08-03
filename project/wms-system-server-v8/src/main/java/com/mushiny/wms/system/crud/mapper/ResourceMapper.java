package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.crud.dto.ResourceDTO;
import com.mushiny.wms.system.domain.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper implements BaseMapper<ResourceDTO, Resource> {

    @Override
    public ResourceDTO toDTO(Resource entity) {
        if (entity == null) {
            return null;
        }

        ResourceDTO dto = new ResourceDTO(entity);

        dto.setResourceKey(entity.getResourceKey());
        dto.setLocale(entity.getLocale());
        dto.setResourceValue(entity.getResourceValue());

        return dto;
    }

    @Override
    public Resource toEntity(ResourceDTO dto) {
        if (dto == null) {
            return null;
        }

        Resource entity = new Resource();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setResourceKey(dto.getResourceKey());
        entity.setLocale(dto.getLocale());
        entity.setResourceValue(dto.getResourceValue());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ResourceDTO dto, Resource entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setLocale(dto.getLocale());
        entity.setResourceValue(dto.getResourceValue());
    }
}
