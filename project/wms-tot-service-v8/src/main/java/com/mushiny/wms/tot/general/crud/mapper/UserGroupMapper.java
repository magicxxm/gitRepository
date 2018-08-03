package com.mushiny.wms.tot.general.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.tot.general.crud.dto.UserGroupDTO;
import com.mushiny.wms.tot.general.domain.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class UserGroupMapper implements BaseMapper<UserGroupDTO, UserGroup> {

    @Override
    public UserGroupDTO toDTO(UserGroup entity) {
        if (entity == null) {
            return null;
        }

        UserGroupDTO dto = new UserGroupDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Override
    public UserGroup toEntity(UserGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        UserGroup entity = new UserGroup();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return entity;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void updateEntityFromDTO(UserGroupDTO dto, UserGroup entity) {
    }
}
