package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.crud.dto.SelectionDTO;
import com.mushiny.wms.system.domain.Selection;
import org.springframework.stereotype.Component;

@Component
public class SelectionMapper implements BaseMapper<SelectionDTO, Selection> {

    @Override
    public SelectionDTO toDTO(Selection entity) {
        if (entity == null) {
            return null;
        }

        SelectionDTO dto = new SelectionDTO(entity);

        dto.setSelectionKey(entity.getSelectionKey());
        dto.setSelectionValue(entity.getSelectionValue());
        dto.setDescription(entity.getDescription());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setResourceKey(entity.getResourceKey());

        return dto;
    }

    @Override
    public Selection toEntity(SelectionDTO dto) {
        if (dto == null) {
            return null;
        }

        Selection entity = new Selection();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setSelectionKey(dto.getSelectionKey());
        entity.setSelectionValue(dto.getSelectionValue());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setResourceKey(dto.getResourceKey());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(SelectionDTO dto, Selection entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setSelectionValue(dto.getSelectionValue());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setResourceKey(dto.getResourceKey());
    }
}
