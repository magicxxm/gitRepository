package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemGroupMapper implements BaseMapper<ItemGroupDTO, ItemGroup> {

    private final ItemGroupRepository itemGroupRepository;

    @Autowired
    public ItemGroupMapper(ItemGroupRepository itemGroupRepository) {
        this.itemGroupRepository = itemGroupRepository;
    }

    @Override
    public ItemGroupDTO toDTO(ItemGroup entity) {
        if (entity == null) {
            return null;
        }

        ItemGroupDTO dto = new ItemGroupDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderIndex(entity.getOrderIndex());

        dto.setParentItemGroup(toDTO(entity.getParentItemGroup()));

        return dto;
    }

    @Override
    public ItemGroup toEntity(ItemGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemGroup entity = new ItemGroup();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());

        if (dto.getParentId() != null) {
            entity.setParentItemGroup(itemGroupRepository.retrieve(dto.getParentId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemGroupDTO dto, ItemGroup entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());

        if (dto.getParentId() != null) {
            entity.setParentItemGroup(itemGroupRepository.retrieve(dto.getParentId()));
        } else {
            entity.setParentItemGroup(null);
        }
    }
}

